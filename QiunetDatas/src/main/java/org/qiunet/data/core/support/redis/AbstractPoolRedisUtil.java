package org.qiunet.data.core.support.redis;

import org.qiunet.utils.data.IKeyValueData;
import redis.clients.jedis.JedisPool;


/**
 * 缓存的管理
 * @author qiunet
 *         Created on 16/12/28 08:28.
 */
public abstract class AbstractPoolRedisUtil extends BasePoolRedisUtil {
	protected AbstractPoolRedisUtil(IKeyValueData<Object, Object> redisProperties, String redisName) {
		super(redisProperties, redisName);
	}

	protected AbstractPoolRedisUtil(JedisPool jedisPool) {
		super(jedisPool);
	}
	/**
	 * Redis 锁.
	 * @param key
	 * @return
	 */
	@Override
	public RedisLock redisLock(String key){
		return new RedisLock(this, key);
	}
}
