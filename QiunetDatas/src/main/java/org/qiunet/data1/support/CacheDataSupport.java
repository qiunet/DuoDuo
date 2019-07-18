package org.qiunet.data1.support;

import org.qiunet.data1.cache.entity.ICacheEntity;
import org.qiunet.data1.cache.status.EntityStatus;
import org.qiunet.data1.core.select.SelectMap;
import org.qiunet.data1.core.support.cache.LocalCache;
import org.qiunet.data1.core.support.db.DefaultDatabaseSupport;


public class CacheDataSupport<Key, Po extends ICacheEntity<Key, Vo>, Vo extends IEntityVo<Po>> extends BaseCacheDataSupport<Po, Vo> {
	/**保存的cache*/
	private LocalCache<Key, Vo> cache = new LocalCache<>();

	public CacheDataSupport(Class<Po> poClass, VoSupplier<Po, Vo> supplier) {
		super(poClass, supplier);
	}


	@Override
	protected void invalidateCache(Po po) {
		cache.invalidate(po.key());
	}

	@Override
	protected void addToCache(Vo vo) {
		Vo newVo = this.cache.putIfAbsent(vo.getPo().key(), vo);
		if (newVo!= null && newVo != vo) {
			throw new RuntimeException("vo exist, and status is ["+newVo.getPo().entityStatus()+"]");
		}
	}

	/***
	 * 对外提供Po对象
	 * @param key
	 * @return
	 */
	public Vo getVo(Key key) {
		Vo vo = cache.get(key);
		if (vo == null) {
			SelectMap map = SelectMap.create().put(defaultPo.keyFieldName(), key);

			Po po = DefaultDatabaseSupport.getInstance().selectOne(selectStatement, map);
			if (po == null) return null;

			po.updateEntityStatus(EntityStatus.NORMAL);
			vo = cache.putIfAbsent(key, supplier.get(po));
		}
		return vo;
	}

	/***
	 * 对指定key的缓存失效
	 * @param keys
	 */
	public void invalidate(Key... keys) {
		cache.invalidateAll(keys);
	}
}
