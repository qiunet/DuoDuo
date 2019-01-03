package org.qiunet.data.core.support;

import org.qiunet.data.core.support.entityInfo.IField;
import org.qiunet.data.core.support.entityInfo.IPlatformEntityInfo;
import org.qiunet.data.db.support.base.DbEntitySupport;
import org.qiunet.data.db.support.base.IDbEntity;
import org.qiunet.data.enums.PlatformType;
import org.qiunet.data.redis.support.info.IPlatFormRedisEntity;
import org.qiunet.data.util.DataUtil;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.threadLocal.ThreadContextData;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author qiunet
 *         Created on 17/2/11 08:38.
 */
public class PlatformEntityDataSupport<DbInfoKey, PO extends IPlatFormRedisEntity, VO> extends BaseDataSupport<PO> {
	private IPlatformEntityInfo<DbInfoKey, PO, VO> entityInfo;

	public PlatformEntityDataSupport(IPlatformEntityInfo<DbInfoKey, PO, VO> entityInfo) {
		super(new DbEntitySupport() , entityInfo);

		this.entityInfo = entityInfo;

		this.selectStatment = entityInfo.getNameSpace() + ".get"+entityInfo.getClazz().getSimpleName();
	}
	/**
	 * update
	 * @param po 需要更新的对象po
	 */
	public void updatePo(PO po) {
		String key = entityInfo.getRedisKey(entityInfo.getDbInfoKey(po), po.getPlatform());
		po.setEntityDbInfo(entityInfo.getEntityDbInfo(po));
		entityInfo.getRedisUtil().setObjectToHash(key, po);

		if (!entityInfo.needAsync() ) {
			dbSupport.update(po, updateStatment);
		}else {
			entityInfo.getRedisUtil().returnJedisProxy().sadd(entityInfo.getAsyncKey(entityInfo.getDbInfoKey(po)), entityInfo.getDbInfoKey(po) +"_"+po.getPlatformName());
		}
	}
	/**
	 * 仅对数值类型的字段生效.
	 * @param po
	 * @param iField
	 * @param changeVal 变动的值. 可以正数 可以负数
	 */
	public void atomicUpdateField(PO po, IField iField, long changeVal) {
		Map<String, Field> fieldMap = DataUtil.getFieldsByClass(po.getClass());
		if (! fieldMap.containsKey(iField.name())) {
			throw new NullPointerException("FieldName ["+iField+"] is not exist in class ["+po.getClass().getSimpleName()+"]");
		}

		Field field = fieldMap.get(iField.name());
		if (! (
				field.getType() == int.class || field.getType() == Integer.class
						|| field.getType() == long.class || field.getType() == Long.class
						|| field.getType() == short.class || field.getType() == Short.class
						|| field.getType() == byte.class || field.getType() == Byte.class
		)) {
			throw new IllegalArgumentException("fieldName[" +iField+ "] is not Integer type");
		}

		String key = entityInfo.getRedisKey(entityInfo.getDbInfoKey(po), po.getPlatform());
		Long ret = entityInfo.getRedisUtil().returnJedisProxy().hincrBy(key, iField.name(), changeVal);
		entityInfo.getRedisUtil().returnJedisProxy().sadd(entityInfo.getAsyncKey(entityInfo.getDbInfoKey(po)), entityInfo.getDbInfoKey(po) +"_"+po.getPlatformName());

		try {
			Method method = DataUtil.getSetMethod(po, iField.name(), field.getType());
			if (field.getType() == int.class || field.getType() == Integer.class) method.invoke(po, ret.intValue());
			else if (field.getType() == short.class || field.getType() == Short.class) method.invoke(po, ret.shortValue());
			else if (field.getType() == byte.class || field.getType() == Byte.class) method.invoke(po, ret.byteValue());
			else method.invoke(po, ret.longValue());
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	/**
	 * insert po
	 * @param po 需要插入的po
	 * @return 1 表示成功
	 */
	public VO insertPo(PO po){
		po.setEntityDbInfo(entityInfo.getEntityDbInfo(po));
		dbSupport.insert(po, insertStatment);

		String key = entityInfo.getRedisKey(entityInfo.getDbInfoKey(po), po.getPlatform());
		entityInfo.getRedisUtil().setObjectToHash(key, po);
		ThreadContextData.put(key, entityInfo.getVo(po));

		return ThreadContextData.get(key);
	}
	/**
	 * 对缓存失效处理
	 */
	public void expireCache(PO po) {
		String key = entityInfo.getRedisKey(entityInfo.getDbInfoKey(po), po.getPlatform());
		ThreadContextData.removeKey(key);
		getRedis().returnJedisProxy().expire(key, 0);
	}
	/**
	 * deletePo
	 * @param po 需要删除的po
	 */
	public void deletePo(PO po) {
		String key = entityInfo.getRedisKey(entityInfo.getDbInfoKey(po), po.getPlatform());
		po.setEntityDbInfo(entityInfo.getEntityDbInfo(po));
		dbSupport.delete(po, deleteStatment);
		ThreadContextData.removeKey(key);
		entityInfo.getRedisUtil().returnJedisProxy().expire(key, 0);
	}

	/**
	 * 得到vo
	 * @param dbInfoKey 分库使用的key  一般uid 或者和platform配合使用
	 * @param platform 平台
	 * @return po的VO对象
	 */
	public VO getVo(DbInfoKey dbInfoKey, PlatformType platform) {
		String key = entityInfo.getRedisKey(dbInfoKey, platform);
		VO vo = ThreadContextData.get(key);
		if (vo != null) return vo;

		PO po = entityInfo.getRedisUtil().getObjectFromHash(key, entityInfo.getClazz());
		if (po == null) {
			po = (PO) ((IDbEntity)dbSupport).selectOne(selectStatment, entityInfo.getEntityDbInfo(dbInfoKey, platform));
			if (po != null) {
				entityInfo.getRedisUtil().setObjectToHash(key , po);
			}
		}
		if (po != null ){
			po.setPlatform(platform);
			vo = entityInfo.getVo(po);
			po.setEntityDbInfo(entityInfo.getEntityDbInfo(po));
			ThreadContextData.put(key, vo);
			return  vo;
		}
		return null;
	}

	@Override
	protected boolean updateToDb(String asyncValue) throws Exception {
		String vals [] = StringUtil.split(asyncValue, "_");
		Object dbInfoKey = vals[0];
		PlatformType platformType = PlatformType.parse(vals[1]);

		String key = entityInfo.getRedisKey(dbInfoKey, platformType);
		PO po = entityInfo.getRedisUtil().getObjectFromHash(key, entityInfo.getClazz());
		if (po != null){
			po.setPlatform(platformType);
			po.setEntityDbInfo(entityInfo.getEntityDbInfo(po));
			dbSupport.update(po, updateStatment);
			return true;
		}
		return false;
	}
}
