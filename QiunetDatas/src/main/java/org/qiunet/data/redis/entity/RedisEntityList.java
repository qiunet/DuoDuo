package org.qiunet.data.redis.entity;

import org.qiunet.data.support.IEntityBo;

public abstract class RedisEntityList<Key, SubKey, Bo extends IEntityBo<? extends RedisEntityList>> extends RedisEntity<Key, Bo> implements IRedisEntityList<Key, SubKey, Bo> {
}
