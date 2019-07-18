package org.qiunet.data1.support;

import org.qiunet.data1.cache.entity.ICacheEntity;
import org.qiunet.data1.cache.status.EntityStatus;
import org.qiunet.data1.core.select.SelectMap;
import org.qiunet.data1.core.support.cache.LocalCache;
import org.qiunet.data1.core.support.db.DefaultDatabaseSupport;


public class CacheDataSupport<Key, Po extends ICacheEntity<Key, Bo>, Bo extends IEntityBo<Po>> extends BaseCacheDataSupport<Po, Bo> {
	/**保存的cache*/
	private LocalCache<Key, Bo> cache = new LocalCache<>();

	public CacheDataSupport(Class<Po> poClass, BoSupplier<Po, Bo> supplier) {
		super(poClass, supplier);
	}


	@Override
	protected void invalidateCache(Po po) {
		cache.invalidate(po.key());
	}

	@Override
	protected void addToCache(Bo bo) {
		Bo newBo = this.cache.putIfAbsent(bo.getPo().key(), bo);
		if (newBo != null && newBo != bo) {
			throw new RuntimeException("bo exist, and status is ["+ newBo.getPo().entityStatus()+"]");
		}
	}

	/***
	 * 对外提供Po对象
	 * @param key
	 * @return
	 */
	public Bo getBo(Key key) {
		Bo bo = cache.get(key);
		if (bo == null) {
			SelectMap map = SelectMap.create().put(defaultPo.keyFieldName(), key);

			Po po = DefaultDatabaseSupport.getInstance().selectOne(selectStatement, map);
			if (po == null) return null;

			po.updateEntityStatus(EntityStatus.NORMAL);
			bo = cache.putIfAbsent(key, supplier.get(po));
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
