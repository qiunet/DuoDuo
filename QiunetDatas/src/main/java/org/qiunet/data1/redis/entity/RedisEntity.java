package org.qiunet.data1.redis.entity;

import org.qiunet.data1.support.DataSupportMapping;
import org.qiunet.data1.support.IDataSupport;
import org.qiunet.data1.support.IEntityBo;

public abstract class RedisEntity<Key, Bo extends IEntityBo<? extends RedisEntity>> implements IRedisEntity<Key, Bo> {
	private transient IDataSupport<RedisEntity, Bo> dataSupport;

	public RedisEntity (){
		this.dataSupport = DataSupportMapping.getMapping(getClass());
	}

	@Override
	public void update() {
		this.dataSupport.update(this);
	}

	@Override
	public void delete() {
		this.dataSupport.delete(this);
	}

	@Override
	public Bo insert() {
		return this.dataSupport.insert(this);
	}
}
