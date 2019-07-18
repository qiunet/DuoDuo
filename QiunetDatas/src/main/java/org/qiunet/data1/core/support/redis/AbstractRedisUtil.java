package org.qiunet.data1.core.support.redis;

import org.qiunet.data1.redis.entity.IRedisEntity;
import org.qiunet.data1.redis.entity.IRedisEntityList;
import org.qiunet.data1.support.IEntityBo;
import org.qiunet.utils.data.IKeyValueData;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.string.StringUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;


/**
 * 缓存的管理
 * @author qiunet
 *         Created on 16/12/28 08:28.
 */
public abstract class AbstractRedisUtil extends BaseRedisUtil {
	protected AbstractRedisUtil(IKeyValueData<Object, Object> redisProperties, String redisName) {
		super(redisProperties, redisName);
	}

	protected AbstractRedisUtil(JedisPool jedisPool) {
		super(jedisPool);
	}
	/**
	 * 频繁请求访问控制
	 * @param key
	 * @return
	 */
	public long redisLock(String key){
		try (Jedis jedis = jedisPool.getResource()){
			long ret = jedis.incr(key);
			jedis.expire(key, 3);
			return ret;
		}
	}

	/***
	 * 获取到do的json
	 * @param key
	 * @return
	 */
	public <Do extends IRedisEntity> Do getDataObjectJson(String key, Class<Do> doClass) {
		try (Jedis jedis = jedisPool.getResource()) {
			String ret = jedis.get(key);
			jedis.expire(key, NORMAL_LIFECYCLE);

			if (StringUtil.isEmpty(ret)) return null;

			return JsonUtil.getGeneralObject(ret, doClass);
		}
	}

	/***
	 * set do的json 到redis
	 * @param key
	 * @return
	 */
	public <Do extends IRedisEntity> void setDataObjectJson(String key, Do aDo) {
		String json = JsonUtil.toJsonString(aDo);
		returnJedisProxy().setex(key, NORMAL_LIFECYCLE, json);
	}

	/***
	 * 得到一个Do List
	 * @param key
	 * @param <Key>
	 * @param <SubKey>
	 * @param <Bo>
	 * @param <Do>
	 * @return
	 */
	public <Key, SubKey, Bo extends IEntityBo, Do extends IRedisEntityList<Key, SubKey, Bo>> List<Do> getDoList(String key){
		List<Do> list = new ArrayList<>();
		try (Jedis jedis = jedisPool.getResource()){

		}
		return list;
	}
}
