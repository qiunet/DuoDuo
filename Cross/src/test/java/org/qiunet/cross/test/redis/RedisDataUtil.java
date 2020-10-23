package org.qiunet.cross.test.redis;

import org.qiunet.data.core.support.redis.BasePoolRedisUtil;
import org.qiunet.data.util.DbProperties;
import redis.clients.jedis.JedisCommands;

/***
 *
 *
 * @author qiunet
 * 2020-10-22 20:49
 */
public class RedisDataUtil extends BasePoolRedisUtil {
	/***
	 * 构造redisUtil 需要的JedisPool
	 */
	private RedisDataUtil() {
		super(DbProperties.getInstance(), "data");
	}

	private static final RedisDataUtil instance = new RedisDataUtil();

	public static JedisCommands getJedis() {
		return instance.returnJedis();
	}

	public static RedisDataUtil getInstance() {
		return instance;
	}
}
