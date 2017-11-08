package org.qiunet.utils.logger.log;

/**
 *  日志的接口
 * Created by qiunet.
 * 17/11/7
 */
public interface QLogger {
	/***
	 * 是否支持debug日志
	 * @return
	 */
	boolean isDebugEnabled();
	/**
	 * 是否支持info日志
	 * @return
	 */
	boolean isInfoEnabled();
	/**
	 * 是否支持warn日志
	 * @return
	 */
	boolean isTraceEnabled();
	/***
	 * debug 日志
	 * @param msg
	 */
	public
	void debug(Object msg);

	/**
	 * debug 级别的异常日志
	 * @param msg
	 * @param t
	 */
	public
	void debug(Object msg, Throwable t);
	/***
	 * info 日志
	 * @param msg
	 */
	public
	void info(Object msg);

	/**
	 * info 级别的异常日志
	 * @param msg
	 * @param t
	 */
	public
	void info(Object msg, Throwable t);

	/***
	 * warn 日志
	 * @param msg
	 */
	public
	void warn(Object msg);

	/**
	 * warn 级别的异常日志
	 * @param msg
	 * @param t
	 */
	public
	void warn(Object msg, Throwable t);
	/***
	 * error 日志
	 * @param msg
	 */
	public
	void error(Object msg);

	/**
	 * info 级别的异常日志
	 * @param msg
	 * @param t
	 */
	public
	void error(Object msg, Throwable t);

	/***
	 * 是否能打印改日志
	 * @param level
	 */
	public
	boolean isEnabledFor(String level);
}
