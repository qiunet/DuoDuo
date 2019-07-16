package org.qiunet.data1.cache.entity;

import org.qiunet.data1.cache.status.EntityStatus;
import org.qiunet.data1.support.DataSupportMapping;
import org.qiunet.data1.support.IDataSupport;
import org.qiunet.data1.support.IEntityVo;

import java.util.concurrent.atomic.AtomicReference;

public abstract class CacheEntity<Key,Vo extends IEntityVo> implements ICacheEntity<Key, Vo> {
	private IDataSupport<CacheEntity, Vo> dataSupport;
	private AtomicReference<EntityStatus> atomicStatus = new AtomicReference<>(EntityStatus.INIT);
	public CacheEntity() {
		this.dataSupport = DataSupportMapping.getMapping(getClass());
	}

	@Override
	public void updateEntityStatus(EntityStatus status) {
		atomicStatus.set(status);
	}

	@Override
	public boolean atomicSetEntityStatus(EntityStatus expect, EntityStatus status) {
		return atomicStatus.compareAndSet(expect, status);
	}

	@Override
	public EntityStatus entityStatus() {
		return atomicStatus.get();
	}

	@Override
	public void update() {
		dataSupport.update(this);
	}

	@Override
	public void delete() {
		dataSupport.delete(this);
	}

	@Override
	public Vo insert() {
		return dataSupport.insert(this);
	}
}
