package org.qiunet.data1.support;

import org.qiunet.data1.async.SyncType;
import org.qiunet.data1.cache.entity.ICacheEntity;
import org.qiunet.data1.cache.status.EntityStatus;
import org.qiunet.data1.core.support.db.DefaultDatabaseSupport;
import org.qiunet.data1.util.DbProperties;

import java.util.concurrent.ConcurrentLinkedQueue;

abstract class BaseCacheDataSupport<Po extends ICacheEntity, Vo extends IEntityVo<Po>> extends BaseDataSupport<Po, Vo> {
	/***对Entity 的操作 **/
	private enum  EntityOperate {INSERT, UPDATE, DELETE}

	protected boolean async = DbProperties.getInstance().getSyncType() == SyncType.ASYNC;
	/***有同步需求的 key*/
	protected ConcurrentLinkedQueue<SyncEntityElement> syncKeyQueue = new ConcurrentLinkedQueue<>();

	protected BaseCacheDataSupport(Class<Po> poClass, VoSupplier<Po ,Vo> supplier) {
		super(poClass, supplier);
	}

	@Override
	public void syncToDatabase() {
		if (!async) return;

		SyncEntityElement element;
		while ((element = syncKeyQueue.poll()) != null) {
			Po po = element.po;
			switch (element.operate) {
				case INSERT:
					if (po.atomicSetEntityStatus(EntityStatus.INSERT, EntityStatus.NORMAL)) {
						DefaultDatabaseSupport.getInstance().insert(insertStatement, po);
					}else {
						logger.error("Entity status ["+po.entityStatus()+"] is error, can not insert to db.");
					}
					break;
				case UPDATE:
					if (po.atomicSetEntityStatus(EntityStatus.UPDATE, EntityStatus.NORMAL)) {
						DefaultDatabaseSupport.getInstance().update(updateStatement, po);
					}else {
						logger.error("Entity status ["+po.entityStatus()+"] is error, can not update to db.");
					}
					break;
				case DELETE:
					DefaultDatabaseSupport.getInstance().delete(deleteStatement, po);
					break;
				default:
					throw new RuntimeException("Db Sync Not Support status: [" + po.entityStatus() + "]");
			}
		}
	}
	/**
	 * 插入
	 * @param po
	 * @return
	 */
	@Override
	public Vo insert(Po po) {
		Vo vo = supplier.get(po);
		if (! async) {
			DefaultDatabaseSupport.getInstance().insert(insertStatement, po);
			return vo;
		}

		if (po.atomicSetEntityStatus(EntityStatus.INIT, EntityStatus.INSERT)){
			syncKeyQueue.add(this.syncQueueElement(po, EntityOperate.INSERT));
			this.addToCache(vo);
		} else {
			logger.error("entity ["+poClass.getName()+"] status ["+po.entityStatus()+"] is error. Not executor insert!");
		}
		return vo;
	}

	/**
	 * 由子类插入缓存中
	 * @param vo
	 */
	protected abstract void addToCache(Vo vo);
	/***
	 * 更新
	 * @param po
	 * @return
	 */
	public int update(Po po) {
		if (! async) {
			return DefaultDatabaseSupport.getInstance().update(updateStatement, po);
		}
		if (po.entityStatus() == EntityStatus.INIT) {
			throw new RuntimeException("Entity must insert first!");
		}

		if (po.atomicSetEntityStatus(EntityStatus.NORMAL, EntityStatus.UPDATE)){
			syncKeyQueue.add(this.syncQueueElement(po, EntityOperate.UPDATE));
		}
		// update 可能update在其它状态的po 所以不需要error打印.
		// insert update 和 delete 状态都不需要操作了,
		return 0;
	}

	/**
	 * 删除
	 * @param po
	 */
	public int delete(Po po) {
		if (po.entityStatus() == EntityStatus.INIT) return 0;

		if (! async) {
			return DefaultDatabaseSupport.getInstance().delete(deleteStatement, po);
		}
		// 直接删除缓存. 异步更新时候, 不校验状态
		this.invalidateCache(po);

		if (po.entityStatus() == EntityStatus.INIT) return 0;
		syncKeyQueue.add(this.syncQueueElement(po, EntityOperate.DELETE));
		return 0;
	}

	/**
	 * 获得同步队列的key
	 * @param po
	 * @return
	 */
	private SyncEntityElement syncQueueElement(Po po, EntityOperate operate){
		return new SyncEntityElement(po, operate);
	}
	/***
	 * 对某个对象失效
	 * @param po
	 */
	protected abstract void invalidateCache(Po po);
	/**
	 * 队列的对象
	 */
	protected class SyncEntityElement {
		private Po po;
		private EntityOperate operate;

		protected SyncEntityElement(Po po, EntityOperate operate) {
			this.po = po;
			this.operate = operate;
		}
	}
}
