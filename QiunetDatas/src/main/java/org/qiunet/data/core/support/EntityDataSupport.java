package org.qiunet.data.core.support;

import org.qiunet.data.core.support.entityInfo.IEntityInfo;
import org.qiunet.data.core.support.entityInfo.IField;
import org.qiunet.data.db.support.base.DbEntitySupport;
import org.qiunet.data.db.support.base.IDbEntity;
import org.qiunet.data.redis.support.info.IRedisEntity;
import org.qiunet.data.util.DataUtil;
import org.qiunet.utils.threadLocal.ThreadContextData;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 统一的方法调用
 * @author qiunet
 *         Created on 17/2/10 17:32.
 */
public class EntityDataSupport<DbInfoKey, PO extends IRedisEntity, VO> extends BaseDataSupport<PO> {

	protected IEntityInfo<DbInfoKey, PO, VO> entityInfo;

	public EntityDataSupport(IEntityInfo<DbInfoKey, PO, VO> entityInfo){
		super(new DbEntitySupport<>(), entityInfo);
		this.entityInfo = entityInfo;
		this.selectStatment = entityInfo.getNameSpace()+".get"+entityInfo.getClazz().getSimpleName();
	}

	/**
	 * update
	 * @param po 需要更新的对象po
	 */
	public void updatePo(PO po) {
		String key = entityInfo.getRedisKey(entityInfo.getDbInfoKey(po));
		po.setEntityDbInfo(entityInfo.getEntityDbInfo(po));
		entityInfo.getRedisUtil().setObjectToHash(key, po);

		if (!entityInfo.needAsync() ) {
			dbSupport.update(po, updateStatment);
		}else {
			entityInfo.getRedisUtil().returnJedisProxy().sadd(entityInfo.getAsyncKey(entityInfo.getDbInfoKey(po)), String.valueOf(entityInfo.getDbInfoKey(po)));
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

		String key = entityInfo.getRedisKey(entityInfo.getDbInfoKey(po));
		Long ret = entityInfo.getRedisUtil().returnJedisProxy().hincrBy(key, iField.name(), changeVal);
		entityInfo.getRedisUtil().returnJedisProxy().sadd(entityInfo.getAsyncKey(entityInfo.getDbInfoKey(po)), String.valueOf(entityInfo.getDbInfoKey(po)));

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

		String key = entityInfo.getRedisKey(entityInfo.getDbInfoKey(po));
		entityInfo.getRedisUtil().setObjectToHash(key, po);

		ThreadContextData.put(key, entityInfo.getVo(po));
		return  ThreadContextData.get(key);
	}

	/**
	 * deletePo
	 * @param po 需要删除的po
	 */
	public void deletePo(PO po) {
		String key = entityInfo.getRedisKey(entityInfo.getDbInfoKey(po));
		po.setEntityDbInfo(entityInfo.getEntityDbInfo(po));
		ThreadContextData.removeKey(key);

		dbSupport.delete(po, deleteStatment);
		entityInfo.getRedisUtil().returnJedisProxy().expire(key, 0);
	}

	/**
	 * 对缓存失效处理
	 */
	public void expireCache(PO po) {
		String key = entityInfo.getRedisKey(entityInfo.getDbInfoKey(po));
		ThreadContextData.removeKey(key);
		getRedis().returnJedisProxy().expire(key, 0);
	}
	/***
	 * 得到vo
	 * @param dbInfoKey 分库使用的key  一般uid
	 * @return po的VO对象
	 */
	public VO getVo(DbInfoKey dbInfoKey){
		String key = entityInfo.getRedisKey(dbInfoKey);
		VO vo = ThreadContextData.get(key);
		if (vo != null) return vo;
		PO po = entityInfo.getRedisUtil().getObjectFromHash(key, entityInfo.getClazz());

		if (po == null){
			po = (PO) ((IDbEntity)dbSupport).selectOne(selectStatment, entityInfo.getEntityDbInfo(dbInfoKey));
			if (po != null) {
				entityInfo.getRedisUtil().setObjectToHash(key, po);
			}
		}

		if (po != null){
			vo = entityInfo.getVo(po);
			po.setEntityDbInfo(entityInfo.getEntityDbInfo(dbInfoKey));
			ThreadContextData.put(key, vo);
			return vo;
		}

		return null;
	}

	@Override
	protected boolean updateToDb(String asyncValue) throws  Exception {
		Object dbInfoKey = asyncValue;

		String key = entityInfo.getRedisKey(dbInfoKey);
		PO po = entityInfo.getRedisUtil().getObjectFromHash(key, entityInfo.getClazz());
		if (po != null){
			po.setEntityDbInfo(entityInfo.getEntityDbInfo(po));
			dbSupport.update(po, updateStatment);
			return true;
		}
		return  false;
	}
}
