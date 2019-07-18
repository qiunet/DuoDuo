package org.qiunet.data1.redis.entity;

import org.qiunet.data1.support.IEntityBo;

public abstract class RedisEntityList<Key, SubKey, Bo extends IEntityBo<? extends RedisEntityList>> extends RedisEntity<Key, Bo> implements IRedisEntityList<Key, SubKey, Bo> {
}
