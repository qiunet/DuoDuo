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
	 * 频繁请求访问控制
	 * @param key
	 * @return
	 */
	public long redisLock(String key){
		return execCommands(jedis -> {
			long ret = jedis.incr(key);
			jedis.expire(key, 3);
			return ret;
		});
	}
}
