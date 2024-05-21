package org.qiunet.data.redis;

public interface IRedisCaller<R> {
	R call(IJedis jedis);
}
