package org.qiunet.data.core.support.entityInfo;

import org.qiunet.data.db.support.info.IEntityDbInfo;
import org.qiunet.data.db.support.info.IEntityListDbInfo;
import org.qiunet.data.redis.support.info.IRedisList;
import org.qiunet.utils.data.CommonData;

/**
 * 需要分表的 List 还需要一个分表 索引
 * @author qiunet
 *         Created on 17/1/25 11:55.
 */
public interface IEntityListInfo<PO extends IRedisList, VO> extends IBaseEntityInfo<PO, VO> {
	/***
	 * redis key
	 * @param dbInfoKey 分库使用的key 这个对象里面, 就是uid
	 * @return redis key
	 */
	public String getRedisKey(Object dbInfoKey);
	/**
	 * 返回分库使用的条件
	 * @param dbInfoKey 分库使用的key 这个对象里面, 就是uid
	 * @param subId subID
	 * @return dbInfo
	 */
	public IEntityListDbInfo getEntityDbInfo(Object dbInfoKey, int subId);
	/**
	 * 得到map 的key
	 * @param po po对象
	 * @return subId
	 */
	public Integer getSubKey(PO po);
}
