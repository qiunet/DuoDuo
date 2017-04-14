package org.qiunet.data.redis.support;

import org.qiunet.data.db.support.info.IEntityListDbInfo;
import org.qiunet.data.redis.support.info.IRedisList;

/**
 *  与个人相关的 list
 *  系统消息  不分平台
 * @author qiunet
 *         Created on 16/12/30 08:08.
 */
public abstract class RedisList extends RedisEntity implements IRedisList {
	
	@Override
	public int getTbIndex() {
		return ((IEntityListDbInfo)entityDbInfo).getTbIndex();
	}
}
