package org.qiunet.data.cache.entity;

import org.qiunet.data.cache.status.EntityStatus;

import java.util.concurrent.atomic.AtomicReference;

public abstract class CacheEntity<Key> implements ICacheEntity<Key> {
	private final transient AtomicReference<EntityStatus> atomicStatus = new AtomicReference<>(EntityStatus.INIT);

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
}
