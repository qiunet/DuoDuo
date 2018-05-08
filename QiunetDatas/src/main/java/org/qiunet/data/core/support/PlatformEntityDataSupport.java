package org.qiunet.data.core.support;

import org.qiunet.data.core.support.entityInfo.IPlatformEntityInfo;
import org.qiunet.data.core.support.update.UpdateFields;
import org.qiunet.data.db.support.base.DbEntitySupport;
import org.qiunet.data.db.support.base.IDbEntity;
import org.qiunet.data.enums.PlatformType;
import org.qiunet.data.redis.support.info.IPlatFormRedisEntity;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.threadLocal.ThreadContextData;
/**
 * @author qiunet
 *         Created on 17/2/11 08:38.
 */
public class PlatformEntityDataSupport<PO extends IPlatFormRedisEntity, VO> extends BaseDataSupport<PO> {
	private IPlatformEntityInfo<PO, VO> entityInfo;

	public PlatformEntityDataSupport(IPlatformEntityInfo<PO, VO> entityInfo) {
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
			entityInfo.getRedisUtil().saddString(entityInfo.getAsyncKey(entityInfo.getDbInfoKey(po)), entityInfo.getDbInfoKey(po) +"_"+po.getPlatformName());
		}
	}
	/**
	 * update
	 * @param po 需要更新的对象po
	 */
	public void updateWithFields(PO po, UpdateFields fields) {
		String key = entityInfo.getRedisKey(entityInfo.getDbInfoKey(po), po.getPlatform());
		po.setEntityDbInfo(entityInfo.getEntityDbInfo(po));
		fields.remove(po.getDbInfoKeyName());
		entityInfo.getRedisUtil().setObjectToHash(key, po, fields.toArray());

		if (!entityInfo.needAsync() ) {
			dbSupport.update(po, updateStatment);
		}else {
			entityInfo.getRedisUtil().saddString(entityInfo.getAsyncKey(entityInfo.getDbInfoKey(po)), entityInfo.getDbInfoKey(po) +"_"+po.getPlatformName());
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
	 * @param dbInfoKey 分库使用的key  一般uid 或者和platform配合使用
	 * @param platform 平台
	 */
	public void expireCache(Object dbInfoKey, PlatformType platform) {
		String key = entityInfo.getRedisKey(dbInfoKey, platform);
		ThreadContextData.removeKey(key);
		getRedis().expire(key, 0);
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
		entityInfo.getRedisUtil().expire(key, 0);
	}

	/**
	 * 得到vo
	 * @param dbInfoKey 分库使用的key  一般uid 或者和platform配合使用
	 * @param platform 平台
	 * @return po的VO对象
	 */
	public VO getVo(Object dbInfoKey, PlatformType platform) {
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
