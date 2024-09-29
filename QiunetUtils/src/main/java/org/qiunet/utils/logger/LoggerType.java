package org.qiunet.utils.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志类型
 * Created by qiunet.
 * 17/8/18
 */
public enum  LoggerType implements ILoggerType {
	/**普通的日志打印*/
	DUODUO("duoduo"),
	/**sql日志打印*/
	DUODUO_SQL("duoduo_sql"),
	/**redis 的 日志打印*/
	DUODUO_REDIS("duoduo_redis"),
	/***热替换**/
	DUODUO_HOTSWAP("duoduo_hotswap"),
	;
	private final String loggerName;

	LoggerType(String loggerName) {
		this.loggerName = loggerName;
	}

	@Override
	public Logger getLogger() {
		return LoggerFactory.getLogger(loggerName);
	}
}
