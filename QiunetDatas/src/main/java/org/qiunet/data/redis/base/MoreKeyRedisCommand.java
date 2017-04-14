package org.qiunet.data.redis.base;

import org.apache.log4j.Logger;
import org.qiunet.utils.string.StringUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Arrays;

/**
 * @author qiunet
 *         Created on 17/2/6 14:34.
 */
public abstract class MoreKeyRedisCommand<T> {
	protected Logger logger = Logger.getLogger(this.getClass());
	
	protected JedisPool pool;
	protected T defaultResult;
	
	public MoreKeyRedisCommand(JedisPool pool){
		this(pool, null);
	}
	public MoreKeyRedisCommand(JedisPool pool, T defaultResult){
		this.pool = pool;
		this.defaultResult = defaultResult;
	}
	/**
	 * 命令名称  错误日志使用
	 * @return 命令名称
	 */
	protected abstract String cmdName();
	/***
	 * 执行表达式
	 * @param jedis jedis 对象
	 * @return 通用的返回结果
	 * @throws Exception 执行可能抛出的redis异常
	 */
	protected abstract T expression(Jedis jedis) throws Exception;
	/**
	 * 返回结果
	 * @return 通用的返回结果
	 */
	public T execAndReturn(){
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return expression(jedis);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			releaseJedis(jedis);
		}
		return defaultResult;
	}

	/**
	 * 使用jedis
	 * @param jedis jedis 对象
	 * @param params 打印需要的参数
	 */
	protected void releaseJedis(Jedis jedis, String... params){
		if(jedis != null){
			try {
				// jedis 自己判断是否是broke的连接
				jedis.close();
			} catch (Exception e) {
				logger.error(StringUtil.format("释放资源:{0}->{1}失败", cmdName(), Arrays.toString(params)), e);
			}
		}
	}
}
