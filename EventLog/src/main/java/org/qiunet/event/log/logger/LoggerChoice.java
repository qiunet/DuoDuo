package org.qiunet.event.log.logger;

import org.qiunet.event.log.enums.RecordModel;
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
//	private static boolean useLog4j;

	static {
		try {
			Class.forName("ch.qos.logback.classic.Logger");
			useLogBack = true;
		} catch (ClassNotFoundException e) {
			useLogBack = false;
		}
//		try {
//			Class.forName("org.apache.log4j.Logger");
//			useLog4j = true;
//		} catch (ClassNotFoundException e) {
//			useLog4j = false;
//		}
	}

	public static ILogger getLogger(RecordModel model, String loggerName) {
		return loggers.computeIfAbsent(loggerName, name -> LoggerChoice.createLogger(model, name));
	}

	private static ILogger createLogger(RecordModel model, String loggerName) {
		switch (model) {
			case LOCAL:
				if (useLogBack) return new LogBackLogger(loggerName);
				else throw new IllegalStateException("Just support for logback");
			case TCP:
				return new TcpLogger(loggerName);
			case UDP:
				return new UdpLogger(loggerName);
			default:
				throw new RuntimeException("not Support for model ["+model+"]");
		}
	}
}
