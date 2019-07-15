package org.qiunet.data1.support;

import org.qiunet.data1.async.SyncType;
import org.qiunet.data1.cache.entity.ICacheEntity;
import org.qiunet.data1.cache.status.EntityStatus;
import org.qiunet.data1.core.support.cache.LocalCache;
import org.qiunet.data1.core.support.db.DefaultDatabaseSupport;
import org.qiunet.data1.util.DbProperties;
import java.util.concurrent.CopyOnWriteArraySet;

class BaseCacheDataSupport<Po extends ICacheEntity> extends BaseDataSupport<Po> {
	private boolean async = DbProperties.getInstance().getSyncType() == SyncType.ASYNC;
	/**保存的cache*/
	protected LocalCache<String, Po> cache = new LocalCache<>();
	/***有同步需求的 key*/
	protected CopyOnWriteArraySet<String> syncKeyQueue = new CopyOnWriteArraySet<>();

	protected BaseCacheDataSupport(Class<Po> poClass) {
		super(poClass);
	}

	@Override
	public void syncToDatabase() {
		if (! async) return;

		syncKeyQueue.forEach(key -> {
			Po po = cache.get(key);
			switch (po.entityStatus()) {
				case INSERT:
					if (po.atomicSetEntityStatus(EntityStatus.INSERT, EntityStatus.NORMAL)) {
						DefaultDatabaseSupport.getInstance().insert(insertStatement, po);
					}
					break;
				case DELETE:
					DefaultDatabaseSupport.getInstance().delete(insertStatement, po);
					cache.invalidate(getCacheKey(po.getKey()));
					break;
				case UPDATE:
					if (po.atomicSetEntityStatus(EntityStatus.UPDATE, EntityStatus.NORMAL)) {
						DefaultDatabaseSupport.getInstance().update(insertStatement, po);
					}
					break;
				default:
					throw new RuntimeException("Not Support status: ["+po.entityStatus()+"]");
			}
		});
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
			syncKeyQueue.add(getCacheKey(po.getKey()));
		}
		return 0;
	}
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
			syncKeyQueue.add(getCacheKey(po.getKey()));
		}
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

		if (po.atomicSetEntityStatus(EntityStatus.DELETE)) {
			syncKeyQueue.add(getCacheKey(po.getKey()));
		}
		return 0;
	}
}
