package org.qiunet.event.log.logger;


import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

/***
 *
 *
 * @author qiunet
 * 2020-04-02 11:06
 ***/
class LogBackLogger implements ILogger {
	private Logger logger;
	LogBackLogger(String loggerName) {
		this.createLogger(loggerName);
	}

	private void createLogger(String loggerName) {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

	}

	@Override
	public String loggerName() {
		return logger.getName();
	}

	@Override
	public void send(String msg) {
		logger.info(msg);
	}
}
