package org.qiunet.data.core.support;

import org.qiunet.data.core.support.entityInfo.IEntityInfo;
import org.qiunet.data.db.support.base.DbEntitySupport;
import org.qiunet.data.db.support.base.IDbEntity;
import org.qiunet.data.redis.support.info.IRedisEntity;
import org.qiunet.utils.threadLocal.ThreadContextData;

/**
 * 统一的方法调用
 * @author qiunet
 *         Created on 17/2/10 17:32.
 */
public class EntityDataSupport<PO extends IRedisEntity, VO> extends BaseDataSupport<PO> {
	
	protected IEntityInfo<PO, VO> entityInfo;
	
	public EntityDataSupport(IEntityInfo<PO, VO> entityInfo){
		super(new DbEntitySupport<PO>(), entityInfo);
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
			entityInfo.getRedisUtil().saddString(entityInfo.getAsyncKey(entityInfo.getDbInfoKey(po)), String.valueOf(entityInfo.getDbInfoKey(po)));
		}
	}
	/**
	 * insert po
	 * @param po 需要插入的po
	 * @return 1 表示成功
	 */
	public int insertPo(PO po){
		po.setEntityDbInfo(entityInfo.getEntityDbInfo(po));
		int ret = dbSupport.insert(po, insertStatment);
		
		String key = entityInfo.getRedisKey(entityInfo.getDbInfoKey(po));
		entityInfo.getRedisUtil().setObjectToHash(key, po);
		
		ThreadContextData.put(key, entityInfo.getVo(po));
		return  ret;
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
		entityInfo.getRedisUtil().expire(key, 0);
	}

	/**
	 * 对缓存失效处理
	 * @param dbInfoKey 分库使用的key  一般uid 或者和platform配合使用
	 */
	public void expireCache(Object dbInfoKey) {
		String key = entityInfo.getRedisKey(dbInfoKey);
		ThreadContextData.removeKey(key);
		getRedis().expire(key, 0);
	}
	/***
	 * 得到vo
	 * @param dbInfoKey 分库使用的key  一般uid 或者和platform配合使用
	 * @return po的VO对象
	 */
	public VO getVo(Object dbInfoKey){
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
