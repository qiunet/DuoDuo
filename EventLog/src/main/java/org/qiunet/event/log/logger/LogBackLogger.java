package org.qiunet.event.log.logger;


import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import org.qiunet.event.log.log.ILogEvent;
import org.qiunet.utils.system.SystemPropertyUtil;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

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
		if (logger != null) {
			return;
		}

		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		this.logger = lc.getLogger(loggerName);
		if (this.logger.iteratorForAppenders().hasNext()) {
			return;
		}

		RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<>();
		appender.setName(loggerName+"Appender");
		appender.setContext(lc);
		appender.setAppend(true);

		TimeBasedRollingPolicy rollingPolicy = new TimeBasedRollingPolicy<>();
		rollingPolicy.setFileNamePattern(SystemPropertyUtil.get("log.dir", "logs") + "/%d{yyyy-MM-dd}/"+loggerName+".log");
		rollingPolicy.setParent(appender);
		rollingPolicy.setContext(lc);
		rollingPolicy.start();

		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setCharset(StandardCharsets.UTF_8);
		encoder.setPattern("%m%n");
		encoder.setContext(lc);
		encoder.start();

		appender.setRollingPolicy(rollingPolicy);
		appender.setEncoder(encoder);
		appender.start();

		AsyncAppender asyncAppender = new AsyncAppender();
		asyncAppender.setContext(lc);
		asyncAppender.setDiscardingThreshold(0);
		asyncAppender.addAppender(appender);
		asyncAppender.setName(loggerName);
		asyncAppender.start();

		logger.addAppender(asyncAppender);
		logger.setLevel(Level.INFO);
		logger.setAdditive(false);
	}

	@Override
	public String loggerName() {
		return logger.getName();
	}

	@Override
	public void send(ILogEvent logEvent) {
		LoggingEvent le = new LoggingEvent(Logger.FQCN, logger, Level.INFO, logEvent.logMessage(), null, null);
		le.setTimeStamp(logEvent.createTime());
		logger.callAppenders(le);
	}
}
