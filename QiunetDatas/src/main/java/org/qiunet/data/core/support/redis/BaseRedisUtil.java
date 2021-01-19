package org.qiunet.data.core.support.redis;

import org.qiunet.utils.data.IKeyValueData;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPoolConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class BaseRedisUtil implements IRedisUtil {
	protected Logger logger = LoggerType.DUODUO_REDIS.getLogger();

	protected String redisName;

	BaseRedisUtil(String redisName) {
		this.redisName = redisName;
	}

	/**
	 * 池配置
	 *
	 * @param redisConfig
	 * @return
	 */
	JedisPoolConfig buildPoolConfig(IKeyValueData<String, String> redisConfig) {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(redisConfig.getInt(getConfigKey("maxIdle"), 30));
		poolConfig.setMaxTotal(redisConfig.getInt(getConfigKey("maxTotal"), 100));
		poolConfig.setTestWhileIdle(redisConfig.getBoolean(getConfigKey("testWhileIdle")));
		poolConfig.setMaxWaitMillis(redisConfig.getInt(getConfigKey("maxWaitMillis"), 3000));
		poolConfig.setNumTestsPerEvictionRun(redisConfig.getInt(getConfigKey("numTestsPerEvictionRun"), 30));
		poolConfig.setMinEvictableIdleTimeMillis(redisConfig.getInt(getConfigKey("minEvictableIdleTimeMillis"), 60000));
		poolConfig.setTimeBetweenEvictionRunsMillis(redisConfig.getInt(getConfigKey("timeBetweenEvictionRunsMillis"), 60000));
		return poolConfig;
	}

	String getConfigKey(String originConfigKey) {
		// 返回类似: redis.{redisName}.host 的字符串
		return "redis." + redisName + "." + originConfigKey;
	}

	/**
	 * Redis 锁.
	 *
	 * @param key
	 * @return
	 */
	@Override
	public RedisLock redisLock(String key) {
		return new RedisLock(this, key);
	}

	protected Object execCommand(Method method, Object[] args, JedisCommands jedis, boolean log) throws IllegalAccessException, InvocationTargetException {
		long startDt = System.currentTimeMillis();
		Object object = method.invoke(jedis, args);
		if (log && logger.isInfoEnabled()) {
			long endDt = System.currentTimeMillis();

			StringBuilder sb = new StringBuilder();
			sb.append("Command[").append(String.format("%-8s", method.getName())).append("]").append(String.format("%2s", (endDt - startDt))).append("ms Key[").append(args[0]).append("] ");
			if (args.length > 1)
				sb.append("\tParams:").append(StringUtil.arraysToString(args, "[", "]", 1, args.length - 1, ",")).append(" ");
			sb.append("\tResult[");
			if (object == null) {
				sb.append("null");
			} else {
				sb.append(JsonUtil.toJsonString(object));
			}
			sb.append("]");
			logger.info(sb.toString());
		}
		return object;
	}
}
