package org.qiunet.data.redis;

import org.qiunet.data.conf.ServerConfig;

public class RedisDataUtil extends BasePoolRedisUtil {
	private static final RedisDataUtil instance = new RedisDataUtil();

	private RedisDataUtil() {
		super(ServerConfig.instance, "data");
	}

	public static RedisDataUtil getInstance() {
		return instance;
	}

	public static IJedis jedis(){
		return instance.returnJedis();
	}

	public static IJedis jedis(boolean log){
		return instance.returnJedis(log);
	}

	public static <T> T executorCommands(IRedisCaller<T> caller) {
		return instance.execCommands(caller);
	}
}
