package org.qiunet.data1.support;

import org.qiunet.data1.core.support.redis.AbstractRedisUtil;
import org.qiunet.data1.redis.entity.IRedisEntityList;

public abstract class RedisDataListSupport<Key, SubKey, Do extends IRedisEntityList<Key, SubKey, Bo>, Bo extends IEntityBo<Do>> extends BaseRedisDataSupport<Do, Bo> {

	public RedisDataListSupport(AbstractRedisUtil redisUtil, Class<Do> doClass, BoSupplier<Do, Bo> supplier) {
		super(redisUtil, doClass, supplier);
	}

}
