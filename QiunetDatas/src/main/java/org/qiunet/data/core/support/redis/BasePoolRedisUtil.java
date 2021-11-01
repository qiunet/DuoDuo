package org.qiunet.data.core.support.redis;

import org.qiunet.utils.data.IKeyValueData;
import org.qiunet.utils.listener.hook.ShutdownHookUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.commands.JedisCommands;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public abstract class BasePoolRedisUtil extends BaseRedisUtil implements IRedisUtil {
	/** 数据源 */
	private final JedisPool jedisPool;
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
		ShutdownHookUtil.getInstance().addLast(this.jedisPool::close);
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
		private final boolean log;
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
			JedisCommands jc = (JedisCommands) Proxy.newProxyInstance(handler.getClass().getClassLoader(), JEDIS_INTERFACES, handler);
			return caller.call(jc);
		}
	}

}
