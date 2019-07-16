package org.qiunet.data1.support;

import org.qiunet.data1.cache.entity.ICacheEntity;
import org.qiunet.data1.cache.status.EntityStatus;
import org.qiunet.data1.core.select.SelectMap;
import org.qiunet.data1.core.support.cache.LocalCache;
import org.qiunet.data1.core.support.db.DefaultDatabaseSupport;


public class CacheDataSupport<Key, Po extends ICacheEntity<Key>, Vo extends IEntityVo<Po>> extends BaseCacheDataSupport<Po, Vo> {
	/**保存的cache*/
	private LocalCache<String, Vo> cache = new LocalCache<>();

	public CacheDataSupport(Class<Po> poClass, VoSupplier<Po, Vo> supplier) {
		super(poClass, supplier);
	}

	@Override
	protected String syncQueueKey(Po po) {
		return getCacheKey(po.key());
	}

	@Override
	protected Po getCachePo(String syncQueueKey) {
		Vo vo = cache.get(syncQueueKey);
		if (vo == null) {
			logger.error("Class ["+poClass.getName()+"] SyncQueueKey ["+syncQueueKey+"] is not exist!");
			return null;
		}
		return vo.getPo();
	}

	@Override
	protected void invalidateCache(Po po) {
		if (po.entityStatus() == EntityStatus.DELETED) {
			cache.invalidate(getCacheKey(po.key()));
		} else {
			logger.error("invalidateCache is error. status ["+po.entityStatus()+"] is not DELETE");
		}
	}

	@Override
	protected void addToCache(Po po) {
		String cacheKey = getCacheKey(po.key());
		this.cache.put(cacheKey, supplier.get(po));
	}

	/***
	 * 对外提供Po对象
	 * @param key
	 * @return
	 */
	public Vo getVo(Key key) {
		String cacheKey = getCacheKey(key);
		Vo vo = cache.get(cacheKey);
		if (vo == null) {
			SelectMap map = SelectMap.create().put(defaultPo.keyFieldName(), key);

			Po po = DefaultDatabaseSupport.getInstance().selectOne(selectStatement, map);
			if (po == null) return null;

			vo = cache.putIfAbsent(cacheKey, supplier.get(po));
		}
		vo.getPo().updateEntityStatus(EntityStatus.NORMAL);
		return vo;
	}
}
