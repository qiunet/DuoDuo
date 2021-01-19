package org.qiunet.data.core.support.redis;

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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public abstract class BasePoolRedisUtil extends BaseRedisUtil implements IRedisUtil {
	private static final Class [] JEDIS_INTERFACES = new Class[]{JedisCommands.class};
	/** 数据源 */
	private JedisPool jedisPool;
	/***
	 * 构造redisUtil 需要的JedisPool
	 * @param redisProperties
	 * @param redisName

	protected BasePoolRedisUtil(IKeyValueData<Object, Object> redisProperties, String redisName) {
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

		ShutdownHookThread.getInstance().addLast(() -> {
			// 添加关闭.
			this.jedisPool.close();
		});
	}
	 */
	/***
	 * 构造redisUtil 需要的JedisPool
	 * @param redisConfig
	 * @param redisName
	 */
	protected BasePoolRedisUtil(IKeyValueData<String, String> redisConfig, String redisName) {
		super(redisName);
		// jedisPool 构造
		this.jedisPool = this.buildJedisPool(redisConfig);
		// 添加关闭.
		ShutdownHookThread.getInstance().addLast(this.jedisPool::close);
	}

	/**
	 * 构造一个可以用的jedisPool
	 * @param redisConfig properties 内容
	 * @return
	 */
	private JedisPool buildJedisPool(IKeyValueData<String, String> redisConfig){

		String host = redisConfig.getString(getConfigKey("host"));
		int port = redisConfig.getInt(getConfigKey("port"));
		String password = redisConfig.getString(getConfigKey("pass"), null);
		if ("".equals(password)) password = null;

		int timeout = redisConfig.getInt(getConfigKey("timeout"), 2000);
		int db = redisConfig.getInt(getConfigKey("db"), 0);
		return new JedisPool(buildPoolConfig(redisConfig), host, port, timeout, password, db, null);
	}

	private class ClosableJedisProxy implements InvocationHandler {
		private boolean log;
		ClosableJedisProxy(boolean log) {
			this.log = log;
		}
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			try (Jedis jedis = jedisPool.getResource()){
				return execCommand(method, args, jedis, log);
			}
		}
	}
	private class NormalJedisProxy implements InvocationHandler {
		private boolean log;
		private JedisCommands jedis;
		NormalJedisProxy(JedisCommands jedis, boolean log) {
			this.log = log;
			this.jedis = jedis;
		}
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			return execCommand(method, args, jedis, log);
		}
	}
	/***
	 * 返回jedis代理
	 * 使用完. 会自己close
	 * @return
	 */
	@Override
	public JedisCommands returnJedis() {
		return returnJedis(true);
	}
	/***
	 * 返回jedis代理
	 * 使用完. 会自己close
	 * @param log true 打印日志 false 不打印日志
	 * @return
	 */
	@Override
	public JedisCommands returnJedis(boolean log) {
		InvocationHandler handler = new ClosableJedisProxy(log);
		return (JedisCommands) Proxy.newProxyInstance(handler.getClass().getClassLoader(), JEDIS_INTERFACES, handler);
	}

	/***
	 * 可以使用caller 在取得一次jedis情况下执行多条命令.
	 * @param caller
	 * @param <T>
	 * @return
	 */
	@Override
	public <T> T execCommands(IRedisCaller<T> caller) {
		try (Jedis jedis = jedisPool.getResource()){
			NormalJedisProxy handler = new NormalJedisProxy(jedis, caller.log());
			JedisCommands jj = (JedisCommands) Proxy.newProxyInstance(handler.getClass().getClassLoader(), JEDIS_INTERFACES, handler);
			T ret = caller.call(jj);
			return ret;
		}
	}

}
