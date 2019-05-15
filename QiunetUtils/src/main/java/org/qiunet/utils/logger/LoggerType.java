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
	DUODUO("duoduo"),
	/**sql日志打印*/
	DUODUO_SQL("duoduo_sql"),
	/**redis 的 日志打印*/
	DUODUO_REDIS("duoduo_redis"),
	/***热替换**/
	DUODUO_HOTSWAP("duoduo_hotswap"),
	;

	private String loggerName;
	LoggerType(String loggerName) {
		this.loggerName = loggerName;
	}

	public Logger getLogger() {
		return LoggerFactory.getLogger(this.loggerName);
	}

	public void info(String msg) {
		getLogger().info(msg);
	}

	public void error(String msg) {
		getLogger().error(msg);
	}

	public void error(String msg, Throwable e) {
		getLogger().error(msg, e);
	}
}
