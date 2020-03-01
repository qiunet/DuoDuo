package org.qiunet.data.cache.entity;

import org.qiunet.data.cache.status.EntityStatus;
import org.qiunet.data.support.DataSupportMapping;
import org.qiunet.data.support.IDataSupport;
import org.qiunet.data.support.IEntityBo;

import java.util.concurrent.atomic.AtomicReference;

public abstract class CacheEntity<Key, Bo extends IEntityBo<? extends CacheEntity>> implements ICacheEntity<Key, Bo> {
	private transient volatile IDataSupport<CacheEntity, Bo> dataSupport;
	private transient volatile AtomicReference<EntityStatus> atomicStatus = new AtomicReference<>(EntityStatus.INIT);

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
	public Bo insert() {
		return dataSupport.insert(this);
	}
}
