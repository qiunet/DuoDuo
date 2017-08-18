package org.qiunet.data.redis.base;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.qiunet.utils.data.StringData;
import org.qiunet.utils.logger.LoggerManager;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Arrays;

/**
 * @author qiunet
 *         Created on 17/2/6 11:44.
 */
public abstract class RedisCommand<T> {
	protected Logger logger = LoggerManager.getInstance().getLogger(LoggerType.QIUNET_DATAS);
	protected JedisPool pool;
	protected T defaultResult;
	private String key;
	public RedisCommand(JedisPool pool, String key){
		this(pool, key, null);
	}

	/**
	 * 有默认值的构造函数
	 * @param pool jedispool
	 * @param key  redis的key
	 * @param defaultResult 默认返回值
	 */
	public RedisCommand(JedisPool pool, String key, T defaultResult){
		this.defaultResult = defaultResult;
		this.pool = pool;
		this.key = key;
	}
	/**
	 * 命令名称  错误日志使用
	 * @return 命令名称
	 */
	protected abstract String cmdName();
	/**
	 * 返回需要打印的参数
	 * @return 需要打印的参数
	 */
	protected Object[] params(){
		return null;
	}

	protected Level getLogLevel() {return Level.INFO; }
	/**
	 * 返回结果
	 * @return 通用返回结果
	 */
	public T execAndReturn(){
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			long startDt = System.currentTimeMillis();
			T ret = expression(jedis, key);
			long endDt = System.currentTimeMillis();
			if (logger.isEnabledFor(getLogLevel())){
				Object params [] = params();
				StringBuilder sb = new StringBuilder();
				sb.append("RedisCommand [").append(String.format("%-18s", cmdName())).append("] ").append(String.format("%3s", (endDt-startDt))).append("ms KEY [").append(key).append("] ");
				if (params != null) sb.append("\t PARAMS ").append(Arrays.toString(params)).append("  ");
				if (ret != null) sb.append("\t RESULT [").append(StringData.parseString(ret)).append("]");
				logger.log(getLogLevel(), sb.toString());
			}
			return ret;
		} catch (Exception e) {
			logger.error(e);
		} finally {
			releaseJedis(jedis, key);
		}
		return defaultResult;
	}
	/***
	 * 执行表达式
	 * @param jedis jedis 对象
	 * @param key redis key
	 * @return 通用的返回结果
	 * @throws Exception 可能抛出redis的异常
	 */
	protected abstract T expression(Jedis jedis, String key) throws Exception;

	/**
	 * 释放连接
	 * @param jedis jedis 对象
	 * @param params 打印的参数
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
