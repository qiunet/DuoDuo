package org.qiunet.utils.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志类型
 * Created by qiunet.
 * 17/8/18
 */
public enum  LoggerType {
	/**普通的日志打印*/
	DUODUO("DuoDuo"),
	/**用时警告等打印*/
	DUODUO_USE_TIME("DuoDuoUseTime"),
	/**sql日志打印*/
	DUODUO_SQL("DuoDuoSql"),
	/**redis 的 日志打印*/
	DUODUO_REDIS("DuoDuoRedis"),
	/***热替换**/
	DUODUO_HOTSWAP("DuoDuoHotSwap"),
	;

	private String loggerName;
	LoggerType(String loggerName) {
		this.loggerName = loggerName;
	}

	public Logger getLogger() {
		return LoggerFactory.getLogger(this.loggerName);
	}


	public void debug(String msg) {
		getLogger().debug(msg);
	}

	public void debug(String format, Object ... arguments) {
		getLogger().debug(format, arguments);
	}


	public void info(String msg) {
		getLogger().info(msg);
	}

	public void info(String format, Object ... arguments) {
		getLogger().info(format, arguments);
	}

	public void warn(String msg) {
		getLogger().warn(msg);
	}

	public void warn(String format, Object ... arguments) {
		getLogger().warn(format, arguments);
	}

	public void error(String msg) {
		getLogger().error(msg);
	}

	public void error(String format, Object ... arguments) {
		getLogger().error(format, arguments);
	}
	public void error(String msg, Throwable e) {
		getLogger().error(msg, e);
	}
}
