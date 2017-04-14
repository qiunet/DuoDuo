package org.qiunet.data.redis.support;

import org.qiunet.data.db.support.info.IEntityListDbInfo;
import org.qiunet.data.redis.support.info.IPlatformRedisList;

/**
 * @author qiunet
 *         Created on 17/2/9 11:20.
 */
public abstract class PlatformRedisList extends PlatformRedisEntity implements IPlatformRedisList {
	@Override
	public int getTbIndex() {
		return ((IEntityListDbInfo)entityDbInfo).getTbIndex();
	}
}
