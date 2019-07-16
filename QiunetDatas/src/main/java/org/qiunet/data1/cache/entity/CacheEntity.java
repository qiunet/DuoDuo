package org.qiunet.data1.cache.entity;

import org.qiunet.data1.cache.status.EntityStatus;
import org.qiunet.data1.support.DataSupportMapping;
import org.qiunet.data1.support.IDataSupport;

import java.util.concurrent.atomic.AtomicReference;

public abstract class CacheEntity<Key> implements ICacheEntity<Key> {
	private IDataSupport<CacheEntity> dataSupport;
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
	public void insert() {
		dataSupport.insert(this);
	}
}
