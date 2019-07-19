package org.qiunet.data1.core.support.redis;

import org.qiunet.utils.data.IKeyValueData;
import org.qiunet.utils.hook.ShutdownHookThread;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

abstract class BaseRedisUtil {
	protected Logger logger = LoggerType.DUODUO_REDIS.getLogger();

	protected final String PLACEHOLDER = "PLACEHOLDER";
	/** 数据源 */
	protected JedisPool jedisPool;

	protected String redisName;
	/***
	 * 构造redisUtil 需要的JedisPool
	 * @param redisProperties
	 * @param redisName
	 */
	protected BaseRedisUtil(IKeyValueData<Object, Object> redisProperties, String redisName) {
		this.redisName = redisName;

		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(redisProperties.getInt(getConfigKey("maxIdle"), 30));
		poolConfig.setMaxTotal(redisProperties.getInt(getConfigKey("maxTotal"), 100));
		poolConfig.setTestWhileIdle(redisProperties.getBoolean(getConfigKey("testWhileIdle")));
		poolConfig.setMaxWaitMillis(redisProperties.getInt(getConfigKey("maxWaitMillis"), 3000));
		poolConfig.setNumTestsPerEvictionRun(redisProperties.getInt(getConfigKey("numTestsPerEvictionRun"), 30));
		poolConfig.setMinEvictableIdleTimeMillis(redisProperties.getInt(getConfigKey("minEvictableIdleTimeMillis"), 60000));
		poolConfig.setTimeBetweenEvictionRunsMillis(redisProperties.getInt(getConfigKey("timeBetweenEvictionRunsMillis"), 60000));

		String host = redisProperties.getString(getConfigKey("host"));
		int port = redisProperties.getInt(getConfigKey("port"));
		String password = redisProperties.getString(getConfigKey("pass"), null);
		if ("".equals(password)) password = null;

		int timeout = redisProperties.getInt(getConfigKey("timeout"), 2000);
		int db = redisProperties.getInt(getConfigKey("db"), 0);

		this.jedisPool = new JedisPool(poolConfig, host, port, timeout, password, db, null);

		ShutdownHookThread.getInstance().addShutdownHook(() -> {
			// 添加关闭.
			this.jedisPool.close();
		});
	}

	protected BaseRedisUtil(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	private String getConfigKey(String originConfigKey) {
		// 返回类似: redis.{redisName}.host 的字符串
		return "redis."+redisName+"."+originConfigKey;
	}

	private class JedisTemp implements InvocationHandler {
		private boolean log;


		JedisTemp(boolean log) {
			this.log = log;
		}
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			try (Jedis jedis = jedisPool.getResource()){
				long startDt = System.currentTimeMillis();

				Object object = method.invoke(jedis, args);

				if (log && logger.isInfoEnabled()){
					long endDt = System.currentTimeMillis();
					StringBuilder sb = new StringBuilder();
					sb.append("RedisCommand [").append(String.format("%-18s", method.getName())).append("] ").append(String.format("%3s", (endDt-startDt))).append("ms KEY [").append(args[0]).append("] ");
					if (args.length > 1) sb.append("\t PARAMS ").append(StringUtil.arraysToString(args, "[", "]", 1, args.length - 1, ",")).append("  ");
					if (object != null) sb.append("\t RESULT [").append(JsonUtil.toJsonString(object)).append("]");
					logger.info(sb.toString());
				}
				return object;
			}
		}
	}
	/***
	 * 返回jedis代理
	 * 使用完. 会自己close
	 * @return
	 */
	public JedisCommands returnJedisProxy() {
		return returnJedisProxy(true);
	}
	/***
	 * 返回jedis代理
	 * 使用完. 会自己close
	 * @param log true 打印日志 false 不打印日志
	 * @return
	 */
	public JedisCommands returnJedisProxy(boolean log) {
		InvocationHandler handler = new JedisTemp(log);
		return (JedisCommands) Proxy.newProxyInstance(handler.getClass().getClassLoader(), Jedis.class.getInterfaces(), handler);
	}

	public Jedis newJedisResource(){
		return jedisPool.getResource();
	}
}
