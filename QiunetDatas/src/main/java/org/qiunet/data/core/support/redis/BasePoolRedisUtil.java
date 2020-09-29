package org.qiunet.data.core.support.redis;

import org.qiunet.listener.hook.ShutdownHookUtil;
import org.qiunet.utils.data.IKeyValueData;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public abstract class BasePoolRedisUtil extends BaseRedisUtil implements IRedisUtil {
	/** 数据源 */
	private JedisPool jedisPool;
	/***
	 * 构造redisUtil 需要的JedisPool
	 * @param redisProperties
	 * @param redisName
	 */
	protected BasePoolRedisUtil(IKeyValueData<Object, Object> redisProperties, String redisName) {
		super(redisName);
		// jedisPool 构造
		this.jedisPool = this.buildJedisPool(redisProperties);
		// 添加关闭.
		ShutdownHookUtil.getInstance().addLast(this.jedisPool::close);
	}
	/**
	 * 构造一个可以用的jedisPool
	 * @param redisProperties properties 内容
	 * @return
	 */
	private JedisPool buildJedisPool(IKeyValueData<Object, Object> redisProperties){

		String host = redisProperties.getString(getConfigKey("host"));
		int port = redisProperties.getInt(getConfigKey("port"));
		String password = redisProperties.getString(getConfigKey("pass"), null);
		if ("".equals(password)) password = null;

		int timeout = redisProperties.getInt(getConfigKey("timeout"), 2000);
		int db = redisProperties.getInt(getConfigKey("db"), 0);
		return new JedisPool(buildPoolConfig(redisProperties), host, port, timeout, password, db, null);
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
