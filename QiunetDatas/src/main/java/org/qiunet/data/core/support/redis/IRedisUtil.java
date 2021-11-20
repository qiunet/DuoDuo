package org.qiunet.data.core.support.redis;


import java.io.IOException;

public interface IRedisUtil {
	/***
	 * 可以使用caller 在取得一次jedis情况下执行多条命令.
	 * @param caller
	 * @param <T>
	 * @return
	 */
	<T> T execCommands(IRedisCaller<T> caller);

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
	 * 使用指定的key指定一段代码.
	 * @param key
	 * @param run
	 * @return 是否锁定成功. 成功则执行了
	 */
	boolean redisLockRun(String key, Runnable run) throws IOException;
	/***
	 * 返回jedis代理
	 * 使用完. 会自己close 默认打印日志
	 * @return
	 */
	default IJedis returnJedis() {
		return returnJedis(true);
	}
}
