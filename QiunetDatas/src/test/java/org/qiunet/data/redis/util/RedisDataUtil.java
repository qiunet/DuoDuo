package org.qiunet.data.redis.util;

import org.qiunet.data.core.support.redis.AbstractPoolRedisUtil;
import org.qiunet.data.core.support.redis.IRedisCaller;
import org.qiunet.data.util.DbProperties;
import redis.clients.jedis.JedisCommands;

public class RedisDataUtil extends AbstractPoolRedisUtil {
	private static final RedisDataUtil instance = new RedisDataUtil();
	private RedisDataUtil() {
		super(DbProperties.getInstance(), "data");
	}

	public static RedisDataUtil getInstance() {
		return instance;
	}

	public static JedisCommands jedis(){
		return instance.returnJedis();
	}

	public static JedisCommands jedis(boolean log){
		return instance.returnJedis(log);
	}

	public static <T> T executorCommands(IRedisCaller<T> caller) {
		return instance.execCommands(caller);
	}
}
