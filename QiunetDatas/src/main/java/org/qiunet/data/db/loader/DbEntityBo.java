package org.qiunet.data.db.loader;

import org.qiunet.data.cache.status.EntityStatus;
import org.qiunet.data.db.entity.DbEntityList;
import org.qiunet.data.db.entity.IDbEntity;
import org.qiunet.data.support.IEntityBo;
import org.qiunet.utils.exceptions.CustomException;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/***
 * 对Bo进行一层封装. 如果是从PlayerDataLoader加载的数据.
 * PlayerDataLoader不等于空. update delete 走cache逻辑.
 *
 * @author qiunet
 * 2021/11/18 19:29
 */
public abstract class DbEntityBo<Do extends IDbEntity> implements IEntityBo<Do> {
	private transient volatile AtomicReference<EntityStatus> atomicStatus = new AtomicReference<>(EntityStatus.INIT);
	PlayerDataLoader playerDataLoader;
	private boolean delete;

	void updateEntityStatus(EntityStatus status) {
		atomicStatus.set(status);
	}

	boolean atomicSetEntityStatus(EntityStatus expect, EntityStatus status) {
		return atomicStatus.compareAndSet(expect, status);
	}

	EntityStatus entityStatus() {
		return atomicStatus.get();
	}

	@Override
	public void update() {
		if (playerDataLoader == null) {
			this.serialize();
			getDo().update();
			return;
		}

		if (entityStatus() == EntityStatus.INIT) {
			throw new CustomException("Need insert first!");
		}

		if (delete) {
			throw new CustomException("Entity already deleted!!");
		}

		if (playerDataLoader.readOnly) {
			throw new CustomException("Data loader read only!");
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

		if (playerDataLoader.readOnly) {
			throw new CustomException("Data loader read only!");
		}

		if (delete) {
			return;
		}

		this.delete = true;
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
