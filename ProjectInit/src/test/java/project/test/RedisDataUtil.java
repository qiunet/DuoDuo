package project.test;

import org.qiunet.data.redis.AbstractRedisUtil;
import redis.clients.jedis.JedisPool;

public class RedisDataUtil extends AbstractRedisUtil {

	/**
	 * @param jedisPool the jedisPool to set
	 */
	public RedisDataUtil(JedisPool jedisPool) {
		super(jedisPool);
	}

	public static AbstractRedisUtil getInstance(){
		return null;
	}
}
