package org.qiunet.data.support;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import org.qiunet.data.core.select.DbParamMap;
import org.qiunet.data.core.support.db.MoreDbSourceDatabaseSupport;
import org.qiunet.data.core.support.redis.IRedisUtil;
import org.qiunet.data.redis.entity.IRedisEntityList;
import org.qiunet.data.redis.entity.RedisEntityByVer;
import org.qiunet.data.redis.util.DbUtil;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.threadLocal.ThreadContextData;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class RedisDataListSupport<Key, SubKey, Do extends IRedisEntityList<Key, SubKey, Bo>, Bo extends IEntityBo<Do>> extends BaseRedisDataSupport<Do, Bo> {
	private final String PLACE_HOLDER = "PLACE_HOLDER";

	public RedisDataListSupport(IRedisUtil redisUtil, Class<Do> doClass, BoSupplier<Do, Bo> supplier) {
		super(redisUtil, doClass, supplier);
	}

	@Override
	protected Do getDoBySyncParams(String syncParams) {
		String[] params = StringUtil.split(syncParams, "#");
		return returnDoFromRedis(getRedisKey(doName, params[0]), params[1]);
	}

	@Override
	protected RedisEntityByVer getDoAndCheckVerBySyncParams(String syncParams) {
		String[] params = StringUtil.split(syncParams, "#");
		return returnDoByVerCheckFromRedis(getRedisKey(doName, params[0]), params[1]);
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

	private RedisEntityByVer returnDoByVerCheckFromRedis(String redisKey, String subKey) {
		return redisUtil.execCommands(jedis -> {
			String json = jedis.hget(redisKey, subKey);
			if (StringUtil.isEmpty(json)) {
				return null;
			}
			jedis.expire(redisKey, NORMAL_LIFECYCLE);

			JSONObject generalObject = JsonUtil.getGeneralObject(json, JSONObject.class);
			Do aDo = JsonUtil.getGeneralObject(json, doClass);
			JSONObject aDoObject = JsonUtil.getGeneralObject(JsonUtil.toJsonString(aDo), JSONObject.class);

			// 比较 generalObject 和 aDoObject 的字段
			boolean check = true;
			for (String key : generalObject.keySet()) {
				if (!aDoObject.containsKey(key)) {
					check = false;
					break;
				}
			}
			RedisEntityByVer redisEntityByVer = new RedisEntityByVer(aDo);
			redisEntityByVer.setRedisJsonAndDoVerCheck(check);
			return redisEntityByVer;
		});
	}

	private List<Do> returnDoListFromRedis(String redisKey) {
		return redisUtil.execCommands(jedis -> {
			List<String> hvals = jedis.hvals(redisKey);
			if (hvals.size() <= 1 && hvals.remove(PLACE_HOLDER)) return null;

			return hvals.parallelStream().map(json -> (Do) JsonUtil.getGeneralObject(json, doClass)).collect(Collectors.toList());
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
		if (doList != null && doList.isEmpty()) {
			DbParamMap paramMap = DbParamMap.create(defaultDo.keyFieldName(), key);
			doList = MoreDbSourceDatabaseSupport.getInstance(DbUtil.getDbSourceKey(key)).selectList(selectStatement, paramMap);

			if (!doList.isEmpty()) {
				this.setListToRedis(redisKey, doList);
			} else {
				// 防止缓存击穿
				redisUtil.execCommands(jedis -> {
					if (jedis.hsetnx(redisKey, PLACE_HOLDER, PLACE_HOLDER) == 1) {
						jedis.expire(redisKey, NORMAL_LIFECYCLE);
					}
					return null;
				});
			}
		}

		if (doList == null || doList.isEmpty()) {
			map = new ConcurrentHashMap<>();
		} else {
			map = doList.parallelStream().collect(Collectors.toConcurrentMap(Do::subKey, aDo -> supplier.get(aDo)));
		}
		ThreadContextData.put(redisKey, map);
		return map;
	}

	@Override
	public Bo insert(Do aDo) {
		String redisKey = getRedisKey(doName, aDo.key());
		Map<SubKey, Bo> boMap = getBoMap(aDo.key());
		if (boMap.isEmpty()) returnJedis().hdel(redisKey, PLACE_HOLDER);

		Bo bo = super.insert(aDo);
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
}
