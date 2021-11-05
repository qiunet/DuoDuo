package org.qiunet.data.core.support.redis;

public interface IRedisCaller<R> {

	R call(IJedis jedis);
	/***
	 * 是否打印日志
	 * @return
	 */
	default boolean log(){
		return true;
	}
}
