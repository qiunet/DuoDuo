package org.qiunet.data1.redis.entity;

import org.qiunet.data1.support.IEntityBo;

public abstract class RedisEntity<Key, Bo extends IEntityBo> implements IRedisEntity<Key, Bo> {


	@Override
	public void update() {

	}

	@Override
	public void delete() {

	}

	@Override
	public Bo insert() {
		return null;
	}
}
