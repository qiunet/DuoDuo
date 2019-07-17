package org.qiunet.data1.support;

import org.qiunet.data1.cache.entity.ICacheEntityList;
import org.qiunet.data1.cache.status.EntityStatus;
import org.qiunet.data1.core.select.SelectMap;
import org.qiunet.data1.core.support.cache.LocalCache;
import org.qiunet.data1.core.support.db.DefaultDatabaseSupport;
import org.qiunet.utils.string.StringUtil;

import java.util.List;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class CacheDataListSupport<Key, SubKey, Po extends ICacheEntityList<Key, SubKey, Vo>, Vo extends IEntityVo<Po>> extends BaseCacheDataSupport<Po, Vo> {
	/**保存的cache*/
	private LocalCache<Key, Map<SubKey, Vo>> cache = new LocalCache<>();

	public CacheDataListSupport(Class<Po> poClass, VoSupplier<Po, Vo> supplier) {
		super(poClass, supplier);
	}

	@Override
	protected void addToCache(Vo vo) {
		Map<SubKey, Vo> map = cache.get(vo.getPo().key());
		if (map == null) {
			throw new NullPointerException("Insert to cache, but map is not exist!");
		}

		Vo newVo = map.putIfAbsent(vo.getPo().subKey(), vo);
		if (newVo!= null && newVo != vo) {
			throw new RuntimeException("vo exist, and status is ["+newVo.getPo().entityStatus()+"]");
		}
	}

	/***
	 * 得到poMap;
	 * @param key
	 * @return
	 */
	public Map<SubKey, Vo> getVoMap(Key key) {
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
		Map<SubKey, Vo> map = cache.get(po.key());
		map.remove(po.subKey());
	}

	/***
	 * 失效
	 * @param key
	 */
	public void invalidate(Key key) {
		cache.invalidate(key);
	}


	@Override
	public Vo insert(Po po) {
		// 先加载所有的数据到缓存
		getVoMap(po.key());

		return super.insert(po);
	}
}
