package org.qiunet.data.support;

import org.qiunet.data.cache.entity.ICacheEntity;
import org.qiunet.data.cache.status.EntityStatus;

import java.util.concurrent.ConcurrentLinkedQueue;

abstract class BaseCacheDataSupport<Do extends ICacheEntity, Bo extends IEntityBo<Do>> extends BaseDataSupport<Do, Bo> {
	/***对Entity 的操作 **/
	private enum  EntityOperate {INSERT, UPDATE, DELETE}

	/***有同步需求的 key*/
	protected ConcurrentLinkedQueue<SyncEntityElement> syncKeyQueue = new ConcurrentLinkedQueue<>();

	protected BaseCacheDataSupport(Class<Do> doClass, BoSupplier<Do, Bo> supplier) {
		super(doClass, supplier);
	}

	@Override
	public void syncToDatabase() {
		if (! async) return;

		SyncEntityElement element;
		while ((element = syncKeyQueue.poll()) != null) {
			Do aDo = element.aDo;
			if (element.operate != EntityOperate.DELETE
				&& aDo.entityStatus() == EntityStatus.DELETE) {
				// 队列后面有delete 操作. 留给delete 操作就行.
				continue;
			}
			switch (element.operate) {
				case INSERT:
					if (aDo.atomicSetEntityStatus(EntityStatus.INSERT, EntityStatus.NORMAL)) {
						databaseSupport(aDo.key()).insert(insertStatement, aDo);
					}else {
						logger.error("Entity status ["+ aDo.entityStatus()+"] is error, can not insert to db.");
					}
					break;
				case UPDATE:
					if (aDo.atomicSetEntityStatus(EntityStatus.UPDATE, EntityStatus.NORMAL)) {
						databaseSupport(aDo.key()).update(updateStatement, aDo);
					}else {
						logger.error("Entity status ["+ aDo.entityStatus()+"] is error, can not update to db.");
					}
					break;
				case DELETE:
					this.deleteDoFromDb(aDo);
					break;
				default:
					throw new RuntimeException("Db Sync Not Support status: [" + aDo.entityStatus() + "]");
			}
		}
	}
	/**
	 * 插入
	 * @param aDo
	 * @return
	 */
	@Override
	public Bo insert(Do aDo) {
		Bo bo = supplier.get(aDo);

		if (aDo.atomicSetEntityStatus(EntityStatus.INIT, EntityStatus.INSERT)){
			if (async) {
				syncKeyQueue.add(new SyncEntityElement(aDo, EntityOperate.INSERT));
			}else {
				databaseSupport(aDo.key()).insert(insertStatement, aDo);
				aDo.updateEntityStatus(EntityStatus.NORMAL);
			}
			this.addToCache(bo);
		} else {
			throw new RuntimeException("entity ["+doClass.getName()+"] status ["+ aDo.entityStatus()+"] is error. Not executor insert!");
		}
		return bo;
	}

	/**
	 * 由子类插入缓存中
	 * @param bo
	 */
	protected abstract void addToCache(Bo bo);
	/***
	 * 更新
	 * @param aDo
	 * @return
	 */
	@Override
	public void update(Do aDo) {
		if (aDo.entityStatus() == EntityStatus.INIT) {
			throw new RuntimeException("Entity must insert first!");
		}

		if (! async) {
			databaseSupport(aDo.key()).update(updateStatement, aDo);
			return;
		}

		if (aDo.atomicSetEntityStatus(EntityStatus.NORMAL, EntityStatus.UPDATE)){
			syncKeyQueue.add(new SyncEntityElement(aDo, EntityOperate.UPDATE));
		}
		// update 可能update在其它状态的po 所以不需要error打印.
		// insert update 和 delete 状态都不需要操作了
	}

	/**
	 * 删除
	 * @param aDo
	 */
	@Override
	public void delete(Do aDo) {
		if (aDo.entityStatus() == EntityStatus.INIT) {
			throw new RuntimeException("Delete entity ["+doClass.getName()+"] It's not insert, Can't delete!");
		}

		if (aDo.entityStatus() == EntityStatus.DELETE) {
			throw new RuntimeException("Delete entity ["+doClass.getName()+"] double times!");
		}

		// 直接删除缓存. 异步更新时候, 不校验状态
		aDo.updateEntityStatus(EntityStatus.DELETE);

		if (async) {
			this.asyncInvalidateCache(aDo);
			syncKeyQueue.add(new SyncEntityElement(aDo, EntityOperate.DELETE));
		}else {
			this.invalidateCache(aDo);
			this.deleteDoFromDb(aDo);
		}
	}

	/***
	 * 对某个对象失效
	 * @param aDo
	 */
	protected abstract void invalidateCache(Do aDo);

	/***
	 * 异步的失效数据
	 * 不会删除 免得删除后 , 异步更新前, 缓存没有. 又从数据库取了还原了.
	 * @param aDo
	 */
	protected abstract void asyncInvalidateCache(Do aDo);
	/***
	 * 从数据库删除do
	 * @param aDo
	 */
	protected abstract void deleteDoFromDb(Do aDo);
	/**
	 * 队列的对象
	 */
	protected class SyncEntityElement {
		private Do aDo;
		private EntityOperate operate;

		protected SyncEntityElement(Do aDo, EntityOperate operate) {
			this.aDo = aDo;
			this.operate = operate;
		}
	}
}
