package org.qiunet.data.support;

import org.qiunet.data.cache.entity.ICacheEntity;
import org.qiunet.data.cache.status.EntityStatus;
import org.qiunet.data.core.support.cache.LocalCache;
import org.qiunet.data.support.anno.LoadAllData;
import org.qiunet.utils.exceptions.CustomException;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

abstract class BaseCacheDataSupport<Do extends ICacheEntity, Bo extends IEntityBo<Do>> extends BaseDataSupport<Do, Bo> {
	/***对Entity 的操作 **/
	private enum  EntityOperate {INSERT, UPDATE, DELETE}

	/***有同步需求的 key*/
	protected ConcurrentLinkedQueue<SyncEntityElement> syncKeyQueue = new ConcurrentLinkedQueue<>();

	protected BaseCacheDataSupport(Class<Do> doClass, BoSupplier<Do, Bo> supplier) {
		super(doClass, supplier);
		boolean localAllData = doClass.isAnnotationPresent(LoadAllData.class);
		this.initCache(localAllData ? LocalCache.createPermanentCache() : LocalCache.createTimeExpireCache());
		if (localAllData) {
			List<Do> objects = databaseSupport().selectList(selectAllStatement, null);
			objects.forEach(aDo -> this.addToCache(supplier.get(aDo)));
		}
	}

	protected abstract void initCache(LocalCache cache);

	@Override
	public void syncToDatabase() {
		if (! async) return;

		SyncEntityElement element;
		while ((element = syncKeyQueue.poll()) != null) {
			Bo bo = element.bo;
			if (element.operate != EntityOperate.DELETE
				&& bo.getDo().entityStatus() == EntityStatus.DELETE) {
				// 队列后面有delete 操作. 留给delete 操作就行.
				continue;
			}
			switch (element.operate) {
				case INSERT:
					if (bo.getDo().atomicSetEntityStatus(EntityStatus.INSERT, EntityStatus.NORMAL)) {
						bo.serialize();
						databaseSupport().insert(insertStatement, bo.getDo());
					}else {
						logger.error("Entity status [{}] is error, can not insert to db.", bo.getDo().entityStatus());
					}
					break;
				case UPDATE:
					if (bo.getDo().atomicSetEntityStatus(EntityStatus.UPDATE, EntityStatus.NORMAL)) {
						bo.serialize();
						databaseSupport().update(updateStatement, bo.getDo());
					}else {
						logger.error("Entity status [{}] is error, can not update to db.", bo.getDo().entityStatus());
					}
					break;
				case DELETE:
					this.deleteDoFromDb(bo.getDo());
					break;
				default:
					throw new CustomException("Db Sync Not Support status: [" + bo.getDo().entityStatus() + "]");
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
				syncKeyQueue.add(new SyncEntityElement(bo, EntityOperate.INSERT));
			}else {
				bo.serialize();
				databaseSupport().insert(insertStatement, aDo);
				aDo.updateEntityStatus(EntityStatus.NORMAL);
			}
			this.addToCache(bo);
		} else {
			throw new CustomException("entity [{}] status [{}] is error. Not executor insert!", doClass.getName(), aDo.entityStatus());
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
	 * @param bo
	 * @return
	 */
	@Override
	public void update(Bo bo) {
		if (bo.getDo().entityStatus() == EntityStatus.INIT) {
			throw new CustomException("Entity must insert first!");
		}

		if (! async) {
			bo.serialize();
			databaseSupport().update(updateStatement, bo.getDo());
			return;
		}

		if (bo.getDo().atomicSetEntityStatus(EntityStatus.NORMAL, EntityStatus.UPDATE)){
			syncKeyQueue.add(new SyncEntityElement(bo, EntityOperate.UPDATE));
		}
		// update 可能update在其它状态的po 所以不需要error打印.
		// insert update 和 delete 状态都不需要操作了
	}

	/**
	 * 删除
	 * @param bo
	 */
	@Override
	public void delete(Bo bo) {
		if (bo.getDo().entityStatus() == EntityStatus.INIT) {
			throw new CustomException("Delete entity [{}] It's not insert, Can't delete!", doClass.getName());
		}

		if (bo.getDo().entityStatus() == EntityStatus.DELETE) {
			throw new CustomException("Delete entity [{}] double times!", doClass.getName());
		}

		// 直接删除缓存. 异步更新时候, 不校验状态
		bo.getDo().updateEntityStatus(EntityStatus.DELETE);

		if (async) {
			this.asyncInvalidateCache(bo.getDo());
			syncKeyQueue.add(new SyncEntityElement(bo, EntityOperate.DELETE));
		}else {
			this.invalidateCache(bo.getDo());
			this.deleteDoFromDb(bo.getDo());
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
		private Bo bo;
		private EntityOperate operate;

		SyncEntityElement(Bo bo, EntityOperate operate) {
			this.bo = bo;
			this.operate = operate;
		}
	}
}
