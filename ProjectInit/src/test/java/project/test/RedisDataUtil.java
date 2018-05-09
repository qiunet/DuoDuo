package project.test;

import org.qiunet.data.redis.AbstractRedisUtil;
import org.qiunet.utils.data.KeyValueData;
import redis.clients.jedis.JedisPool;

public class RedisDataUtil extends AbstractRedisUtil {

	/**
	 */
	public RedisDataUtil() {
		super(new KeyValueData<>(), "data");
	}

	public static AbstractRedisUtil getInstance(){
		return null;
	}
}
