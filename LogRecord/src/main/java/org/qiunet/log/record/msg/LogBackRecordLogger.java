package org.qiunet.log.record.msg;


import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import com.google.common.collect.Maps;
import org.qiunet.log.record.enums.ILogRecordType;
import org.qiunet.log.record.logger.IRecordLogger;
import org.qiunet.utils.system.SystemPropertyUtil;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/***
 * Log back 记录日志
 *
 * @author qiunet
 * 2020-04-02 11:06
 ***/
enum LogBackRecordLogger implements IRecordLogger {
	instance;

	private final Map<String, Logger> loggers = Maps.newConcurrentMap();

	private synchronized Logger createLogger(String loggerName) {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		Logger logger = lc.getLogger(loggerName);
		if (logger.iteratorForAppenders().hasNext()) {
			return logger;
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
		return logger;
	}

	@Override
	public String loggerName() {
		return "logbackRecord";
	}

	@Override
	public <T extends Enum<T> & ILogRecordType, L extends LogRecordMsg<T>>  void send(L logRecordMsg) {
		Logger logger = loggers.computeIfAbsent(logRecordMsg.logType().getLogRecordName(), this::createLogger);
		LoggingEvent le = new LoggingEvent(Logger.FQCN, logger, Level.INFO, logRecordMsg.logMessage(), null, null);
		le.setTimeStamp(logRecordMsg.createTime());
		logger.callAppenders(le);
	}
}
