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
	private LocalCache<String, Map<String, Vo>> cache = new LocalCache<>();

	public CacheDataListSupport(Class<Po> poClass, VoSupplier<Po, Vo> supplier) {
		super(poClass, supplier);
	}

	@Override
	protected void addToCache(Vo vo) {
		String key = String.valueOf(vo.getPo().key());
		Map<String, Vo> map = cache.get(key);
		if (map == null) {
			throw new NullPointerException("Insert to cache, but map is not exist!");
		}

		Vo newVo = map.putIfAbsent(String.valueOf(vo.getPo().subKey()), vo);
		if (newVo!= null && newVo != vo) {
			throw new RuntimeException("vo exist, and status is ["+newVo.getPo().entityStatus()+"]");
		}
	}

	@Override
	protected String syncQueueKey(Po po) {
		return getCacheKey(po.key(), po.subKey());
	}

	@Override
	protected Po getCachePo(String syncQueueKey) {
		String [] keys = StringUtil.split(syncQueueKey, "#");
		Map<String, Vo> poMap = cache.get(keys[0]);
		if (poMap == null || ! poMap.containsKey(keys[1])) {
			logger.error("Class ["+poClass.getName()+"]SyncQueueKey ["+syncQueueKey+"] is not exist!");
			return null;
		}
		return poMap.get(keys[1]).getPo();
	}

	/***
	 * 得到poMap;
	 * @param key
	 * @return
	 */
	public Map<SubKey, Vo> getVoMap(Key key) {
		try {
			Map<String, Vo> voMap = cache.get(String.valueOf(key), () -> {
				SelectMap map = SelectMap.create().put(defaultPo.keyFieldName(), key);
				List<Po> poList = DefaultDatabaseSupport.getInstance().selectList(selectStatement, map);

				return poList.parallelStream()
					.peek(po -> po.updateEntityStatus(EntityStatus.NORMAL))
					.collect(Collectors.toMap(po -> String.valueOf(po.subKey()), po -> supplier.get(po)));
			});

			return voMap.values().parallelStream().collect(Collectors.toConcurrentMap(vo -> vo.getPo().subKey(), vo -> vo));
		} catch (ExecutionException e) {
			logger.error("GetPo Key ["+key+"] Exception: ", e);
		}
		return null;
	}

	@Override
	protected void invalidateCache(Po po) {
		if (po.entityStatus() == EntityStatus.DELETED) {
			Map<String, Vo> map = cache.get(String.valueOf(po.key()));
			map.remove(String.valueOf(po.subKey()));
		} else {
			logger.error("invalidateCache is error. status ["+po.entityStatus()+"] is not DELETE");
		}
	}

	/***
	 * 失效
	 * @param key
	 */
	public void invalidate(Key key) {
		cache.invalidate(String.valueOf(key));
	}


	@Override
	public Vo insert(Po po) {
		// 先加载所有的数据到缓存
		getVoMap(po.key());

		return super.insert(po);
	}
}
