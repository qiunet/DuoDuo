package org.qiunet.data.redis.entity;

import org.qiunet.data.core.entity.IEntity;
import org.qiunet.data.support.IEntityBo;

public interface IRedisEntity<Key, Bo extends IEntityBo> extends IEntity<Key, Bo> {
}
