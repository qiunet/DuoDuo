package org.qiunet.data.db.loader;

import org.qiunet.data.cache.status.EntityStatus;
import org.qiunet.data.db.entity.DbEntityList;
import org.qiunet.data.db.entity.IDbEntity;
import org.qiunet.data.support.IEntityBo;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/***
 *
 * @author qiunet
 * 2021/11/18 19:29
 */
public abstract class DbEntityBo<Do extends IDbEntity> implements IDbEntityBo<Do> {
	private transient volatile AtomicReference<EntityStatus> atomicStatus = new AtomicReference<>(EntityStatus.INIT);
	PlayerDataLoader playerDataLoader;

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
		if (playerDataLoader == null) {
			this.serialize();
			getDo().update();
			return;
		}
		// 如果上次的数据没有逻辑. 比如插入. 则这次更新不进行
		if (atomicStatus.compareAndSet(EntityStatus.NORMAL, EntityStatus.UPDATE)) {
			playerDataLoader.cacheAsyncToDb.add(PlayerDataLoader.EntityOperate.UPDATE,this);
		}
	}

	@Override
	public void delete() {
		if (playerDataLoader == null) {
			getDo().update();
			return;
		}

		EntityStatus entityStatus = entityStatus();
		Object obj = playerDataLoader.dataCache.get(getClass());
		if (obj instanceof Map) {
			((Map<?, ?>) obj).remove(((DbEntityList) getDo()).subKey());
		}else {
			playerDataLoader.dataCache.put(getClass(), PlayerDataLoader.NULL);
		}

		// 尚未入库
		if (entityStatus == EntityStatus.INIT) {
			return;
		}
		playerDataLoader.cacheAsyncToDb.add(PlayerDataLoader.EntityOperate.DELETE,this);
	}
}
