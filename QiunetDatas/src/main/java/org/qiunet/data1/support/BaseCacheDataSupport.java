package org.qiunet.data1.support;

import org.qiunet.data1.async.SyncType;
import org.qiunet.data1.cache.entity.ICacheEntity;
import org.qiunet.data1.cache.status.EntityStatus;
import org.qiunet.data1.core.support.db.DefaultDatabaseSupport;
import org.qiunet.data1.util.DbProperties;

import java.util.Iterator;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

abstract class BaseCacheDataSupport<Po extends ICacheEntity, Vo> extends BaseDataSupport<Po, Vo> {
	protected boolean async = DbProperties.getInstance().getSyncType() == SyncType.ASYNC;
	/***有同步需求的 key*/
	protected ConcurrentSkipListSet<String> syncKeyQueue = new ConcurrentSkipListSet<>();

	protected BaseCacheDataSupport(Class<Po> poClass, VoSupplier<Po ,Vo> supplier) {
		super(poClass, supplier);
	}

	@Override
	public void syncToDatabase() {
		if (!async) return;

		String key;
		while ((key = syncKeyQueue.pollFirst()) != null) {
			Po po = getCachePo(key);
			switch (po.entityStatus()) {
				case INSERT:
					if (po.atomicSetEntityStatus(EntityStatus.INSERT, EntityStatus.NORMAL)) {
						DefaultDatabaseSupport.getInstance().insert(insertStatement, po);
					}else {
						logger.error("Entity status ["+po.entityStatus()+"] is error, can not insert to db.");
					}
					break;
				case DELETE:
					if (po.atomicSetEntityStatus(EntityStatus.DELETE, EntityStatus.DELETED)) {
						DefaultDatabaseSupport.getInstance().delete(deleteStatement, po);
						this.invalidateCache(po);
					}else {
						logger.error("Entity status ["+po.entityStatus()+"] is error, can not delete from db.");
					}
					break;
				case UPDATE:
					if (po.atomicSetEntityStatus(EntityStatus.UPDATE, EntityStatus.NORMAL)) {
						DefaultDatabaseSupport.getInstance().update(updateStatement, po);
					}else {
						logger.error("Entity status ["+po.entityStatus()+"] is error, can not update to db.");
					}
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
	public int insert(Po po) {
		if (! async) {
			return DefaultDatabaseSupport.getInstance().insert(insertStatement, po);
		}

		if (po.atomicSetEntityStatus(EntityStatus.INIT, EntityStatus.INSERT)){
			syncKeyQueue.add(this.syncQueueKey(po));
			this.addToCache(po);
		} else {
			logger.error("entity status ["+po.entityStatus()+"] is error. Not executor insert!");
		}
		return 0;
	}
	protected abstract void addToCache(Po po);
	/***
	 * 更新
	 * @param po
	 * @return
	 */
	public int update(Po po) {
		if (! async) {
			return DefaultDatabaseSupport.getInstance().update(updateStatement, po);
		}

		if (po.atomicSetEntityStatus(EntityStatus.NORMAL, EntityStatus.UPDATE)){
			syncKeyQueue.add(this.syncQueueKey(po));
		}
		// update 可能update在其它状态的po 所以不需要error打印.
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

		po.updateEntityStatus(EntityStatus.DELETE);
		syncKeyQueue.add(this.syncQueueKey(po));
		return 0;
	}

	/**
	 * 获得同步队列的key
	 * @param po
	 * @return
	 */
	protected abstract String syncQueueKey(Po po);
	/**
	 * 从子类获得cache的Po
	 * @param syncQueueKey
	 * @return
	 */
	protected abstract Po getCachePo(String syncQueueKey);
	/***
	 * 对某个对象失效
	 * @param po
	 */
	protected abstract void invalidateCache(Po po);

	protected String getCacheKey(Object... keys) {
		StringJoiner sj = new StringJoiner("#");
		for (Object key : keys) {
			sj.add(String.valueOf(key));
		}
		return sj.toString();
	}
}
