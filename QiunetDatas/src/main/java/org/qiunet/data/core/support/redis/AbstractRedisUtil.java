package org.qiunet.data.core.support.redis;

import org.qiunet.utils.data.IKeyValueData;
import redis.clients.jedis.JedisPool;


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
	 * Redis 锁.
	 * @param key
	 * @return
	 */
	public RedisLock redisLock(String key){
		return new RedisLock(this, key);
	}
}
