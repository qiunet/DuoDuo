package org.qiunet.data1.redis.entity;

import org.qiunet.data1.support.IEntityVo;

public abstract class RedisEntity<Key, Vo extends IEntityVo> implements IRedisEntity<Key, Vo> {


	@Override
	public void update() {

	}

	@Override
	public void delete() {

	}

	@Override
	public Vo insert() {
		return null;
	}
}
