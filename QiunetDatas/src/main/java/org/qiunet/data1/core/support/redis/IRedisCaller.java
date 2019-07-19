package org.qiunet.data1.core.support.redis;

import redis.clients.jedis.JedisCommands;

public interface IRedisCaller<R> {

	R call(JedisCommands jedis);
	/***
	 * 是否打印日志
	 * @return
	 */
	default boolean log(){
		return true;
	}
}
