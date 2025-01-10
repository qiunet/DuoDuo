package org.qiunet.data.core.support.redis;

import org.qiunet.data.util.ServerConfig;
import redis.clients.jedis.JedisCommands;

public interface IRedisCaller<R> {
	final boolean redisLogOpen = ServerConfig.isRedisLogOpen();

	R call(JedisCommands jedis);
	/***
	 * 是否打印日志
	 * @return
	 */
	default boolean log(){
		return redisLogOpen;
	}
}
