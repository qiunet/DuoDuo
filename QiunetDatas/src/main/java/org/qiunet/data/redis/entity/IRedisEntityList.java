package org.qiunet.data.redis.entity;

import org.qiunet.data.core.entity.IEntityList;

public interface IRedisEntityList<Key, SubKey> extends IEntityList<Key, SubKey>, IRedisEntity<Key> {

}
