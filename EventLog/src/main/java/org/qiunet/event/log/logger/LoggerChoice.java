package org.qiunet.event.log.logger;

import org.qiunet.event.log.enums.RecordModel;
import org.qiunet.event.log.enums.base.IEventLogType;
import org.qiunet.utils.exceptions.CustomException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/***
 * 根据能找到的包. 选择日志Logger
 *
 * @author qiunet
 * 2020-03-25 10:02
 ***/
public final class LoggerChoice {
	private static Map<String, ILogger> loggers = new ConcurrentHashMap<>();

	private static boolean useLogBack;

	static {
		try {
			Class.forName("ch.qos.logback.classic.Logger");
			useLogBack = true;
		} catch (ClassNotFoundException e) {
			useLogBack = false;
		}
	}

	public static ILogger getLogger(IEventLogType logType) {
			return LoggerChoice.createLogger(logType.recordModel(), logType.getLoggerName());
	}

	private static ILogger createLogger(RecordModel model, String loggerName) {
		switch (model) {
			case LOCAL:
				if (useLogBack) return loggers.computeIfAbsent(loggerName, LogBackLogger::new);
				else throw new IllegalStateException("Just support for logback");
//			case TCP:
//				return TcpLogger.instance;
//			case UDP:
//				return UdpLogger.instance;
			default:
				throw new CustomException("not Support for model [{}]", model);
		}
	}
}
