package org.qiunet.data1.support;

import org.qiunet.data1.cache.entity.ICacheEntity;
import org.qiunet.data1.cache.status.EntityStatus;
import org.qiunet.data1.core.select.SelectMap;
import org.qiunet.data1.core.support.cache.LocalCache;
import org.qiunet.data1.core.support.db.DefaultDatabaseSupport;


public class CacheDataSupport<Key, Do extends ICacheEntity<Key, Bo>, Bo extends IEntityBo<Do>> extends BaseCacheDataSupport<Do, Bo> {
	/**保存的cache*/
	private LocalCache<Key, Bo> cache = new LocalCache<>();

	public CacheDataSupport(Class<Do> doClass, BoSupplier<Do, Bo> supplier) {
		super(doClass, supplier);
	}


	@Override
	protected void invalidateCache(Do aDo) {
		cache.invalidate(aDo.key());
	}

	@Override
	protected void addToCache(Bo bo) {
		Bo newBo = this.cache.putIfAbsent(bo.getDo().key(), bo);
		if (newBo != null && newBo != bo) {
			throw new RuntimeException("bo exist, and status is ["+ newBo.getDo().entityStatus()+"]");
		}
	}

	/***
	 * 对外提供Bo对象
	 * @param key
	 * @return
	 */
	public Bo getBo(Key key) {
		Bo bo = cache.get(key);
		if (bo == null) {
			SelectMap map = SelectMap.create().put(defaultDo.keyFieldName(), key);

			Do aDo = DefaultDatabaseSupport.getInstance().selectOne(selectStatement, map);
			if (aDo == null) return null;

			aDo.updateEntityStatus(EntityStatus.NORMAL);
			bo = cache.putIfAbsent(key, supplier.get(aDo));
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
