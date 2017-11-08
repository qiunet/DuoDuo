package org.qiunet.utils.logger;

import org.qiunet.utils.logger.log.Log4j1Logger;
import org.qiunet.utils.logger.log.Log4j2Logger;
import org.qiunet.utils.logger.log.QLogger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日志logger 管理
 * Created by qiunet.
 * 17/8/18
 */
public final class LoggerManager {
	private static final Map<String, QLogger> loggerMap = new ConcurrentHashMap<>();
	private static final boolean use_log4j2; //   是否使用log4j2 了
	static {
		use_log4j2 = LoggerManager.class.getResource("/log4j2.xml") != null;
	}
	private LoggerManager() {}
	/***
	 * 得到自己的日志
	 * @param loggerType
	 * @return
	 */
	public static QLogger getLogger(LoggerType loggerType) {
		return getLogger(loggerType.name());
	}
	/***
	 * 得到自己的日志
	 * @param loggerName
	 * @return
	 */
	public static QLogger getLogger(String loggerName) {
		if (! loggerMap.containsKey(loggerName)) {
			if (!use_log4j2) {
				org.apache.log4j.Logger logger = org.apache.log4j.LogManager.getLogger(loggerName);
				loggerMap.put(loggerName, new Log4j1Logger(logger));
			} else {
				org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(loggerName);
				loggerMap.put(loggerName, new Log4j2Logger(logger));
			}
		}
		return loggerMap.get(loggerName);
	}
}
