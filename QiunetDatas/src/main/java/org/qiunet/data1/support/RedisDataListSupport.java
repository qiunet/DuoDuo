package org.qiunet.data1.support;

import com.google.common.base.Preconditions;
import org.qiunet.data1.core.select.DbParamMap;
import org.qiunet.data1.core.support.db.MoreDbSourceDatabaseSupport;
import org.qiunet.data1.core.support.redis.AbstractRedisUtil;
import org.qiunet.data1.redis.entity.IRedisEntityList;
import org.qiunet.data1.redis.util.DbUtil;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.threadLocal.ThreadContextData;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class RedisDataListSupport<Key, SubKey, Do extends IRedisEntityList<Key, SubKey, Bo>, Bo extends IEntityBo<Do>> extends BaseRedisDataSupport<Do, Bo> {

	public RedisDataListSupport(AbstractRedisUtil redisUtil, Class<Do> doClass, BoSupplier<Do, Bo> supplier) {
		super(redisUtil, doClass, supplier);
	}

	@Override
	protected Do getDoBySyncParams(String syncParams) {
		String [] params = StringUtil.split(syncParams, "#");
		return returnDoFromRedis(getRedisKey(doName, params[0]), params[1]);
	}

	@Override
	protected void deleteFromDb(Do aDo) {
		DbParamMap map = DbParamMap.create()
			.put(defaultDo.keyFieldName(), aDo.key())
			.put(defaultDo.subKeyFieldName(), aDo.subKey())
			.put("tbIndex", aDo.getTbIndex())
			.put("dbName", aDo.getDbName());
		MoreDbSourceDatabaseSupport.getInstance(aDo.getDbSourceKey()).delete(deleteStatement, map);
	}

	@Override
	protected String buildSyncParams(Do aDo) {
		return getRedisKey(aDo.key(), aDo.subKey());
	}

	@Override
	protected void setDataObjectToRedis(Do aDo) {
		String redisKey = getRedisKey(doName, aDo.key());
		redisUtil.execCommands(jedis -> {
			jedis.hset(redisKey, String.valueOf(aDo.subKey()), JsonUtil.toJsonString(aDo));
			jedis.expire(redisKey, NORMAL_LIFECYCLE);
			return null;
		});
	}

	@Override
	protected void delFromRedis(Do aDo) {
		String redisKey = getRedisKey(doName, aDo.key());
		returnJedis().hdel(redisKey, String.valueOf(aDo.subKey()));
	}

	private Do returnDoFromRedis(String redisKey, String subKey) {
		return redisUtil.execCommands(jedis -> {
			String json = jedis.hget(redisKey, subKey);
			jedis.expire(redisKey, NORMAL_LIFECYCLE);
			return JsonUtil.getGeneralObject(json, doClass);
		});
	}
	private List<Do> returnDoListFromRedis(String redisKey) {
		return redisUtil.execCommands(jedis -> {
			List<String> hvals = jedis.hvals(redisKey);
			return hvals.parallelStream().map(json -> JsonUtil.getGeneralObject(json, doClass)).collect(Collectors.toList());
		});
	}


	private void setListToRedis(String redisKey, List<Do> doList) {
		redisUtil.execCommands(jedis -> {
			doList.forEach(aDo -> jedis.hset(redisKey, String.valueOf(aDo.subKey()), JsonUtil.toJsonString(aDo)));
			jedis.expire(redisKey, NORMAL_LIFECYCLE);
			return null;
		});
	}
	/***
	 * 得到bo的map
	 * @param key
	 * @return
	 */
	public Map<SubKey, Bo> getBoMap(Key key) {
		String redisKey = getRedisKey(doName, key);

		Map<SubKey, Bo> map = ThreadContextData.get(redisKey);
		if (map != null) return map;

		List<Do> doList = returnDoListFromRedis(redisKey);
		if (doList.isEmpty()) {
			DbParamMap paramMap = DbParamMap.create(defaultDo.keyFieldName(), key);
			doList = MoreDbSourceDatabaseSupport.getInstance(DbUtil.getDbSourceKey(key)).selectList(selectStatement, paramMap);

			if (! doList.isEmpty()) {
				this.setListToRedis(redisKey, doList);
			}
		}

		if (doList.isEmpty()) {
			map = new ConcurrentHashMap<>();
		}else {
			map = doList.parallelStream().collect(Collectors.toConcurrentMap(Do::subKey, aDo -> supplier.get(aDo)));
		}
		ThreadContextData.put(redisKey, map);
		return map;
	}

	@Override
	public Bo insert(Do aDo) {
		getBoMap(aDo.key());
		Bo bo = super.insert(aDo);

		String redisKey = getRedisKey(doName, aDo.key());
		Map<SubKey, Bo> map = ThreadContextData.get(redisKey);
		Preconditions.checkNotNull(map);
		map.putIfAbsent(aDo.subKey(), bo);
		return bo;
	}

	@Override
	public void delete(Do aDo) {
		String redisKey = getRedisKey(doName, aDo.key());
		Map<SubKey, Bo> map = ThreadContextData.get(redisKey);
		Preconditions.checkNotNull(map);
		map.remove(aDo.subKey());
		super.delete(aDo);
	}

	/***
	 * 失效缓存
	 * @param key
	 */
	public void expire(Key key) {
		String redisKey = getRedisKey(doName, key);
		ThreadContextData.removeKey(redisKey);

		returnJedis().expire(redisKey, 0);
	}
}
