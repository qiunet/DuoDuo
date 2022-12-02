package org.qiunet.data.support;

import org.qiunet.data.cache.entity.ICacheEntityList;
import org.qiunet.data.cache.status.EntityStatus;
import org.qiunet.data.core.select.DbParamMap;
import org.qiunet.data.core.support.cache.LocalCache;
import org.qiunet.utils.exceptions.CustomException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/***
 *  List 的结构本身是防止缓存击穿的.
 *  没有数据, 会在缓存放一个空的Map
 * @param <Key>
 * @param <SubKey>
 * @param <Do>
 * @param <Bo>
 */
public class CacheDataListSupport<Key, SubKey, Do extends ICacheEntityList<Key, SubKey>, Bo extends IEntityBo<Do>> extends BaseCacheDataSupport<Key, Do, Bo> {
	/**保存的cache*/
	private LocalCache<Key, Map<SubKey, Bo>> cache;

	public CacheDataListSupport(Class<Do> doClass, BoSupplier<Do, Bo> supplier) {
		super(doClass, supplier);
	}

	@Override
	protected void initCache(boolean loadAll) {
		this.cache = loadAll ? LocalCache.createPermanentCache() : LocalCache.createTimeExpireCache();
	}

	@Override
	protected void addToCache(Bo bo) {
		Map<SubKey, Bo> map = cache.get(bo.getDo().key());
		if (map == null) {
			throw new NullPointerException("Insert to cache, but map is not exist!");
		}

		Bo newBo = map.putIfAbsent(bo.getDo().subKey(), bo);
		if (newBo != null && newBo != bo) {
			throw new CustomException("bo exist, and status is [{}]", newBo.getDo().entityStatus());
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
				DbParamMap map = DbParamMap.create(table, table.keyName(), key);
				List<Do> doList = databaseSupport().selectList(selectStatement, map);

				return doList.stream()
					.peek(aDo -> aDo.updateEntityStatus(EntityStatus.NORMAL))
					.collect(Collectors.toConcurrentMap(Do::subKey, aDo -> supplier.get(aDo)));
			});
		} catch (ExecutionException e) {
			logger.error("GetBo Key ["+key+"] Exception: ", e);
		}
		return null;
	}

	@Override
	protected void invalidateCache(Do aDo) {
		Map<SubKey, Bo> map = cache.get(aDo.key());
		map.remove(aDo.subKey());
	}

	@Override
	protected void asyncInvalidateCache(Do aDo) {
		this.invalidateCache(aDo);
	}

	@Override
	protected void deleteDoFromDb(Do aDo) {
		DbParamMap map = DbParamMap.create(table, table.keyName(), aDo.key())
			.put(table.subKeyName(), aDo.subKey());
		databaseSupport().delete(deleteStatement, map);
	}

	/***
	 * 对指定key的缓存失效
	 * @param keys
	 */
	public void invalidate(Key... keys) {
		cache.invalidateAll(keys);
	}


	@Override
	public Bo insert(Do aDo) {
		// 先加载所有的数据到缓存
		getBoMap(aDo.key());

		return super.insert(aDo);
	}
}
