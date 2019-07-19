package org.qiunet.data1.redis;

import org.qiunet.data1.core.support.redis.AbstractRedisUtil;
import org.qiunet.data1.util.DbProperties;
import redis.clients.jedis.JedisCommands;

public class RedisDataUtil extends AbstractRedisUtil {
	private static final RedisDataUtil instance = new RedisDataUtil();
	private RedisDataUtil() {
		super(DbProperties.getInstance(), "data");
	}

	public static JedisCommands getInstance() {
		return instance.returnJedisProxy();
	}
}
