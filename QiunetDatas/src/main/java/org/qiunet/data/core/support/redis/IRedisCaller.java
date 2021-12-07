package org.qiunet.data.core.support.redis;

public interface IRedisCaller<R> {
	R call(IJedis jedis);
}
