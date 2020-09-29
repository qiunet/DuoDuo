package org.qiunet.data.core.support.redis;

import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;
import redis.clients.jedis.JedisCommands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class BaseRedisUtil implements IRedisUtil {
	protected Logger logger = LoggerType.DUODUO_REDIS.getLogger();

	protected String redisName;
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
	protected String getConfigKey(String originConfigKey) {
		return "redis."+redisName+"."+originConfigKey;
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

	protected Object execCommand(Method method, Object[] args, JedisCommands jedis, boolean log) throws IllegalAccessException, InvocationTargetException {
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
