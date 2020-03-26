package org.qiunet.event.log.logger;

import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/***
 * 根据能找到的包. 选择日志Logger
 *
 * @author qiunet
 * 2020-03-25 10:02
 ***/
public final class LoggerChoice {
	private Logger logger = LoggerType.DUODUO.getLogger();

	private static Map<String, ILogger> loggers = new ConcurrentHashMap<>();

	private static boolean useLogBack;
	private static boolean useLog4j;

	static {
		try {
			Class.forName("ch.qos.logback.classic.Logger");
			useLogBack = true;
		} catch (ClassNotFoundException e) {
			useLogBack = false;
		}
		try {
			Class.forName("org.apache.log4j.Logger");
			useLog4j = true;
		} catch (ClassNotFoundException e) {

			useLog4j = false;
		}
	}

	public static ILogger getLogger(String name) {
		return loggers.computeIfAbsent(name, LoggerChoice::createLogger);
	}

	private static ILogger createLogger(String name) {
		return null;
	}
}
