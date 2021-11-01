package org.qiunet.data.core.support.redis;

import org.qiunet.utils.data.IKeyValueData;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.commands.JedisCommands;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;

public abstract class BaseRedisUtil implements IRedisUtil {
	 static final Class [] JEDIS_INTERFACES = new Class[]{JedisCommands.class};
	protected Logger logger = LoggerType.DUODUO_REDIS.getLogger();

	private final String redisName;

	 BaseRedisUtil(String redisName) {
		this.redisName = redisName;
	}

	/**
	 * 池配置
	 * @param redisConfig
	 * @return
	 */
	JedisPoolConfig buildPoolConfig(IKeyValueData<String, String> redisConfig) {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(redisConfig.getInt(getConfigKey("maxIdle"), 30));
		poolConfig.setMaxTotal(redisConfig.getInt(getConfigKey("maxTotal"), 100));
		poolConfig.setTestWhileIdle(redisConfig.getBoolean(getConfigKey("testWhileIdle")));
		poolConfig.setMaxWait(Duration.ofMillis(redisConfig.getInt(getConfigKey("maxWait"), 3000)));
		poolConfig.setNumTestsPerEvictionRun(redisConfig.getInt(getConfigKey("numTestsPerEvictionRun"), 30));
		poolConfig.setMinEvictableIdleTime(Duration.ofMillis(redisConfig.getInt(getConfigKey("minEvictableIdleTime"), 60000)));
		poolConfig.setTimeBetweenEvictionRuns(Duration.ofMillis(redisConfig.getInt(getConfigKey("timeBetweenEvictionRuns"), 60000)));
		return poolConfig;
	}
	/**
	 * Redis 锁.
	 * @param key
	 * @return
	 */
	@Override
	public RedisLock redisLock(String key){
		return new RedisLock(this, key);
	}

	/**
	 * 拼接类似: redis.{redisName}.host 的字符串
	 * @param originConfigKey
	 * @return
	 */
	 String getConfigKey(String originConfigKey) {
		return "redis."+redisName+"."+originConfigKey;
	}


	protected class NormalJedisProxy implements InvocationHandler {
		private final boolean log;
		private final JedisCommands jedis;
		NormalJedisProxy(JedisCommands jedis, boolean log) {
			this.log = log;
			this.jedis = jedis;
		}
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			return execCommand(method, args, jedis, log);
		}
	}

	@Override
	public boolean redisLockRun(String key, Runnable run) {
		try (RedisLock lock = redisLock(key)) {
			if (lock.lock()) {
				run.run();
				return true;
			}
		}
		return false;
	}

	 Object execCommand(Method method, Object[] args, JedisCommands jedis, boolean log) throws IllegalAccessException, InvocationTargetException {
		long startDt = System.currentTimeMillis();
		Object object = method.invoke(jedis, args);
		if (log && logger.isInfoEnabled()){
			long endDt = System.currentTimeMillis();

			StringBuilder sb = new StringBuilder();
			sb.append("Command[").append(String.format("%-8s", method.getName())).append("]").append(String.format("%2s", (endDt-startDt))).append("ms Key[").append(args[0]).append("] ");
			if (args.length > 1) sb.append("\tParams:").append(StringUtil.arraysToString(args, "[", "]", 1, args.length - 1, ",")).append(" ");
			sb.append("\tResult[");
			if (object == null) {
				sb.append("null");
			}else {
				sb.append(JsonUtil.toJsonString(object));
			}
			sb.append("]");
			logger.info(sb.toString());
		}
		return object;
	}
}
