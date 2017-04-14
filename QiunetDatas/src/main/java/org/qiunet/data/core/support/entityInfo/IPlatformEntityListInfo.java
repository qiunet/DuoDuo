package org.qiunet.data.core.support.entityInfo;

import org.qiunet.data.db.support.info.IEntityDbInfo;
import org.qiunet.data.db.support.info.IPlatformEntityListDbInfo;
import org.qiunet.data.enums.PlatformType;
import org.qiunet.data.redis.support.info.IPlatformRedisList;
import org.qiunet.utils.data.CommonData;

/**
 * @author qiunet
 *         Created on 17/2/11 09:46.
 */
public interface IPlatformEntityListInfo<PO extends IPlatformRedisList, VO> extends IBaseEntityInfo<PO,VO> {
	/**
	 * 返回分库使用的条件
	 * @param dbInfoKey 分库使用的key 这个对象里面, 就是uid
	 * @param platform 平台
	 * @param subId sub id                   
	 * @return dbInfo
	 */
	public IPlatformEntityListDbInfo getEntityDbInfo(Object dbInfoKey, PlatformType platform, int subId);
	/**
	 * 得到map 的key
	 * @param po po
	 * @return 返回 除了dbINfoKey 外能确定一个对象的那个key 值
	 */
	public Integer getSubKey(PO po);
	
	/***
	 * redis key
	 * @param dbInfoKey 分库使用的key 这个对象里面, 就是uid
	 * @param platform 平台                    
	 * @return redis key
	 */
	public String getRedisKey(Object dbInfoKey, PlatformType platform);
}
