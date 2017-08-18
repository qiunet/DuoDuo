package org.qiunet.utils.logger;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日志logger 管理
 * Created by qiunet.
 * 17/8/18
 */
public class LoggerManager {
	private Map<String, Logger> loggerMap = new ConcurrentHashMap<>();
	private volatile static LoggerManager instance;

	private LoggerManager() {
		if (instance != null) throw new RuntimeException("Instance Duplication!");
		instance = this;
	}

	public static LoggerManager getInstance() {
		if (instance == null) {
			synchronized (LoggerManager.class) {
				if (instance == null)
				{
					new LoggerManager();
				}
			}
		}
		return instance;
	}
	/***
	 * 得到自己的日志
	 * @param loggerType
	 * @return
	 */
	public Logger getLogger(LoggerType loggerType) {
		return getLogger(loggerType.name());
	}
	/***
	 * 得到自己的日志
	 * @param loggerName
	 * @return
	 */
	public Logger getLogger(String loggerName) {
		if (! loggerMap.containsKey(loggerName)) {
			Logger logger = Logger.getLogger(loggerName);
			loggerMap.put(loggerName, logger);
		}
		return loggerMap.get(loggerName);
	}

}
