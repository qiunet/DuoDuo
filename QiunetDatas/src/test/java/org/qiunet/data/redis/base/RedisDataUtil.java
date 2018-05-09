package org.qiunet.data.redis.base;

import org.qiunet.data.redis.AbstractRedisUtil;
import org.qiunet.utils.data.IKeyValueData;
import org.qiunet.utils.properties.LoaderProperties;
import org.qiunet.utils.properties.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Properties;

/**
 * @author qiunet
 *         Created on 17/1/5 08:30.
 */
public class RedisDataUtil extends AbstractRedisUtil {
	private volatile static RedisDataUtil instance;

	private RedisDataUtil(String propertiesName) {
		super(PropertiesUtil.loadPropertiesFromResourcesPath(propertiesName), "data");
		instance = this;
	}
	public static RedisDataUtil getInstance() {
		if (instance == null) {
			new RedisDataUtil("redis.properties");
		}
		return instance;
	}
}
