package org.qiunet.data1.support;

import org.qiunet.data1.cache.entity.ICacheEntityList;
import org.qiunet.data1.cache.status.EntityStatus;
import org.qiunet.data1.core.select.SelectMap;
import org.qiunet.data1.core.support.cache.LocalCache;
import org.qiunet.data1.core.support.db.DefaultDatabaseSupport;

import java.util.List;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class CacheDataListSupport<Key, SubKey, Po extends ICacheEntityList<Key, SubKey, Bo>, Bo extends IEntityBo<Po>> extends BaseCacheDataSupport<Po, Bo> {
	/**保存的cache*/
	private LocalCache<Key, Map<SubKey, Bo>> cache = new LocalCache<>();

	public CacheDataListSupport(Class<Po> poClass, BoSupplier<Po, Bo> supplier) {
		super(poClass, supplier);
	}

	@Override
	protected void addToCache(Bo bo) {
		Map<SubKey, Bo> map = cache.get(bo.getPo().key());
		if (map == null) {
			throw new NullPointerException("Insert to cache, but map is not exist!");
		}

		Bo newBo = map.putIfAbsent(bo.getPo().subKey(), bo);
		if (newBo != null && newBo != bo) {
			throw new RuntimeException("bo exist, and status is ["+ newBo.getPo().entityStatus()+"]");
		}
	}

	/***
	 * 得到poMap;
	 * @param key
	 * @return
	 */
	public Map<SubKey, Bo> getBoMap(Key key) {
		try {
			return cache.get(key, () -> {
				SelectMap map = SelectMap.create().put(defaultPo.keyFieldName(), key);
				List<Po> poList = DefaultDatabaseSupport.getInstance().selectList(selectStatement, map);

				return poList.parallelStream()
					.peek(po -> po.updateEntityStatus(EntityStatus.NORMAL))
					.collect(Collectors.toConcurrentMap(Po::subKey, po -> supplier.get(po)));
			});
		} catch (ExecutionException e) {
			logger.error("GetPo Key ["+key+"] Exception: ", e);
		}
		return null;
	}

	@Override
	protected void invalidateCache(Po po) {
		Map<SubKey, Bo> map = cache.get(po.key());
		map.remove(po.subKey());
	}

	/***
	 * 对指定key的缓存失效
	 * @param keys
	 */
	public void invalidate(Key... keys) {
		cache.invalidateAll(keys);
	}


	@Override
	public Bo insert(Po po) {
		// 先加载所有的数据到缓存
		getBoMap(po.key());

		return super.insert(po);
	}
}
