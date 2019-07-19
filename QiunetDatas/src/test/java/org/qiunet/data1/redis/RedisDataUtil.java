package org.qiunet.data1.redis;

import org.qiunet.data1.core.support.redis.AbstractRedisUtil;
import org.qiunet.data1.core.support.redis.IRedisCaller;
import org.qiunet.data1.util.DbProperties;
import redis.clients.jedis.JedisCommands;

public class RedisDataUtil extends AbstractRedisUtil {
	private static final RedisDataUtil instance = new RedisDataUtil();
	private RedisDataUtil() {
		super(DbProperties.getInstance(), "data");
	}

	public static RedisDataUtil getInstance() {
		return instance;
	}

	public static JedisCommands returnJedis(){
		return instance.returnJedisProxy();
	}

	public static JedisCommands returnJedis(boolean log){
		return instance.returnJedisProxy(log);
	}

	public static <T> T executorCommands(IRedisCaller<T> caller) {
		return instance.execCommands(caller);
	}
}
