package org.qiunet.data.core.support.redis;


public interface IRedisUtil {
	/***
	 * 可以使用caller 在取得一次jedis情况下执行多条命令.
	 * @param caller
	 * @param <T>
	 * @return
	 */
	default <T> T execCommands(IRedisCaller<T> caller) {
		return execCommands(caller, true);
	}
	/***
	 * 可以使用caller 在取得一次jedis情况下执行多条命令.
	 * @param caller
	 * @param log true 打印日志 false 不打印
	 * @param <T>
	 * @return
	 */
	<T> T execCommands(IRedisCaller<T> caller, boolean log);
	/***
	 * 返回jedis代理
	 * 使用完. 会自己close
	 * @param log true 打印日志 false 不打印日志
	 * @return
	 */
	IJedis returnJedis(boolean log);
	/***
	 * 得到redis的Lock
	 * @param key
	 * @return
	 */
	RedisLock redisLock(String key);
	/**
	 * 异步在锁里执行数据
	 * @param key key
	 * @param call 执行代码
	 */
	void asyncRedisLockRun(String key, Runnable call);
	/***
	 * 返回jedis代理
	 * 使用完. 会自己close 默认打印日志
	 * @return
	 */
	default IJedis returnJedis() {
		return returnJedis(true);
	}
}
