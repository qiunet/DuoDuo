package project.test;

import org.qiunet.data.core.support.redis.BasePoolRedisUtil;
import org.qiunet.data.core.support.redis.IRedisUtil;
import org.qiunet.utils.data.KeyValueData;

public class RedisDataUtil extends BasePoolRedisUtil {

	/**
	 */
	public RedisDataUtil() {
		super(new KeyValueData<>(), "data");
	}

	public static IRedisUtil getInstance(){
		return null;
	}
}
