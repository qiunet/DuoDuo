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
public abstract class DbEntityBo<Do extends IDbEntity<?>> implements IEntityBo<Do> , IDbEntityBo {
	private final transient AtomicReference<EntityStatus> atomicStatus = new AtomicReference<>(EntityStatus.INIT);
	private final boolean syncImmediately = getClass().isAnnotationPresent(SyncImmediately.class);
	PlayerDataLoader playerDataLoader;

	@Override
	public void updateEntityStatus(EntityStatus status) {
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
		if (playerDataLoader == null || playerDataLoader.offline || syncImmediately) {
			IEntityBo.super.update();
			return;
		}

		if (!playerDataLoader.sync.inSelfThread()) {
			throw new RuntimeException("Not in self thread!");
		}

		if (entityStatus() == EntityStatus.INIT) {
			throw new CustomException("Need insert first!");
		}

		if (entityStatus() == EntityStatus.DELETE) {
			throw new CustomException("Entity already deleted!!");
		}

		// 如果上次的数据没有逻辑. 比如插入. 则这次更新不进行
		if (atomicStatus.compareAndSet(EntityStatus.NORMAL, EntityStatus.UPDATE)) {
			playerDataLoader.cacheAsyncToDb.add(PlayerDataLoader.EntityOperate.UPDATE,this);
		}
	}

	@Override
	public void delete() {
		EntityStatus status = entityStatus();
		if (status == EntityStatus.DELETE) {
			return;
		}

		if (! atomicStatus.compareAndSet(status, EntityStatus.DELETE)) {
			// 可能INSERT 执行完毕, 变成NORMAL了. 确保修改是原子性的.
			this.delete();
			return;
		}

		// 尚未入库
		if (status == EntityStatus.INIT) {
			return;
		}

		if (playerDataLoader == null) {
			IEntityBo.super.delete();
			return;
		}

		Object obj = playerDataLoader.dataCache.get(getClass());
		if (obj instanceof Map) {
			((Map<?, ?>) obj).remove(((DbEntityList<?, ?>) getDo()).subKey());
		}else {
			playerDataLoader.dataCache.put(getClass(), PlayerDataLoader.NULL);
		}

		// 尚未入库
		if (playerDataLoader.offline || syncImmediately) {
			// 离线用户直接删除
			IEntityBo.super.delete();
			return;
		}

		playerDataLoader.cacheAsyncToDb.add(PlayerDataLoader.EntityOperate.DELETE,this);
	}
}
