package org.qiunet.data.redis.base;

import org.qiunet.data.redis.AbstractRedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author qiunet
 *         Created on 17/1/5 08:30.
 */
public class RedisDataUtil extends AbstractRedisUtil {
	private volatile static RedisDataUtil instance;
	
	private RedisDataUtil(JedisPool jedisPool) {
		super(jedisPool);
		instance = this;
	}
	public static RedisDataUtil getInstance() {
		if (instance == null) {
			JedisPoolConfig poolConfig = new JedisPoolConfig();
			poolConfig.setMaxWaitMillis(1000);
			poolConfig.setMaxTotal(50);
			poolConfig.setMinIdle(10);
			JedisPool pool = new JedisPool(poolConfig,"127.0.0.1", 6389);
			new RedisDataUtil(pool);
		}
		return instance;
	}
}
