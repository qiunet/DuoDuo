package org.qiunet.game.tests.server.redis;

import org.qiunet.data.conf.ServerConfig;
import org.qiunet.data.redis.BasePoolRedisUtil;
import org.qiunet.data.redis.IJedis;

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
		super(ServerConfig.getInstance(), "data");
	}

	private static final RedisDataUtil instance = new RedisDataUtil();

	public static IJedis getJedis() {
		return instance.returnJedis();
	}

	public static RedisDataUtil getInstance() {
		return instance;
	}
}
