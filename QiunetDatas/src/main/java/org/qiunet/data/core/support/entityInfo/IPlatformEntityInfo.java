package org.qiunet.data.core.support.entityInfo;

import org.qiunet.data.db.support.info.IPlatformEntityDbInfo;
import org.qiunet.data.enums.PlatformType;
import org.qiunet.data.redis.support.info.IPlatFormRedisEntity;

/**
 * @author qiunet
 *         Created on 17/2/11 09:39.
 */
public interface IPlatformEntityInfo<PO extends IPlatFormRedisEntity, VO> extends IBaseEntityInfo<PO, VO> {
	/**
	 * 得到key
	 * @param dbInfoKey 分库使用的key 一般uid
	 * @param platform 平台
	 * @return rediskey
	 */
	public String getRedisKey(Object dbInfoKey, PlatformType platform);
	/**
	 * 返回分库使用的条件
	 * @param dbInfoKey 分库使用的key 一般uid
	 * @param platform 平台                    
	 * @return dbINfo
	 */
	public IPlatformEntityDbInfo getEntityDbInfo(Object dbInfoKey, PlatformType platform);
}
