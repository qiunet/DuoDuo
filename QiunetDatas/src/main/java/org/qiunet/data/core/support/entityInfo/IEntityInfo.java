package org.qiunet.data.core.support.entityInfo;

import org.qiunet.data.db.support.info.IEntityDbInfo;
import org.qiunet.data.redis.AbstractRedisUtil;
import org.qiunet.data.redis.support.info.IRedisEntity;

/**
 * /**
 * pojo 相对于data对象的补充数据. 和每个
 * @author qiunet
 *         Created on 17/1/25 11:52.
 */
public interface IEntityInfo<PO extends IRedisEntity, VO> extends IBaseEntityInfo<PO, VO> {
	/***
	 *  得到redis key
	 *  @param dbInfoKey 分库使用的key 这个对象里面, 就是uid
	 * @return redis key
	 */
	public String getRedisKey(Object dbInfoKey);
	/** 
	 * 返回分库使用的条件
	 * @param dbInfoKey 分库使用的key 这个对象里面, 就是uid
	 * @return 返回dbInfo
	 */
	public IEntityDbInfo getEntityDbInfo(Object dbInfoKey);
}
