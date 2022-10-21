package org.qiunet.data.support;

import org.qiunet.data.cache.entity.ICacheEntity;
import org.qiunet.data.cache.status.EntityStatus;
import org.qiunet.data.core.select.DbParamMap;
import org.qiunet.data.core.support.cache.LocalCache;
import org.qiunet.utils.exceptions.CustomException;


public class CacheDataSupport<Key, Do extends ICacheEntity<Key>, Bo extends IEntityBo<Do>> extends BaseCacheDataSupport<Key, Do, Bo> {
	/**防止缓存击穿的 NULL值*/
	private final Bo NULL;
	/**保存的cache*/
	private LocalCache<Key, Bo> cache;

	public CacheDataSupport(Class<Do> doClass, BoSupplier<Do, Bo> supplier) {
		super(doClass, supplier);
		this.NULL = supplier.get(defaultDo);
	}

	@Override
	protected void initCache(boolean loadAll) {
		this.cache = loadAll ? LocalCache.createPermanentCache() : LocalCache.createTimeExpireCache();
	}

	@Override
	protected void invalidateCache(Do aDo) {
		cache.invalidate(aDo.key());
	}

	@Override
	protected void asyncInvalidateCache(Do aDo) {
		cache.put(aDo.key(), NULL);
	}

	@Override
	protected void deleteDoFromDb(Do aDo) {
		DbParamMap map = DbParamMap.create(table, defaultDo.keyFieldName(), aDo.key());
		databaseSupport().delete(deleteStatement, map);
	}

	@Override
	protected void addToCache(Bo bo) {
		Key key = bo.getDo().key();
		if (! cache.replace(key, NULL, bo)) {
			Bo newBo = this.cache.putIfAbsent(key, bo);
			if (newBo != null && newBo != bo) {
				throw new CustomException("bo exist, and status is [{}]", newBo.getDo().entityStatus());
			}
		}
	}

	/***
	 * 对外提供Bo对象
	 * @param key
	 * @return
	 */
	public Bo getBo(Key key) {
		Bo bo = cache.get(key);
		if (bo == NULL) return null;

		if (bo == null) {
			DbParamMap map = DbParamMap.create(table, defaultDo.keyFieldName(), key);

			Do aDo = databaseSupport().selectOne(selectStatement, map);
			if (aDo == null) {
				cache.putIfAbsent(key, NULL);
				return null;
			}

			aDo.updateEntityStatus(EntityStatus.NORMAL);
			cache.putIfAbsent(key, supplier.get(aDo));
			bo = cache.get(key);
		}
		return bo;
	}

	/***
	 * 对指定key的缓存失效
	 * @param keys
	 */
	public void invalidate(Key... keys) {
		cache.invalidateAll(keys);
	}
}
