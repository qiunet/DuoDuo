package org.qiunet.log.record.logger;


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
import org.qiunet.log.record.content.StringLogContentGetter;
import org.qiunet.log.record.enums.ILogRecordType;
import org.qiunet.log.record.msg.ILogRecordMsg;
import org.qiunet.utils.system.SystemPropertyUtil;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/***
 * Log back 记录日志
 * 主要是放服务器. 自己查询自己看.
 * 如果有其它平台需要. 请自己实现 {@link IRecordLogger}
 *
 * @author qiunet
 * 2020-04-02 11:06
 ***/
public class LogBackRecordLogger implements IBasicRecordLogger {
	private static final StringLogContentGetter getter = new StringLogContentGetter("=", " | ");
	protected static final 	LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
	protected final Map<String, Logger> loggers = Maps.newConcurrentMap();

	protected synchronized Logger createLogger(String loggerName) {
		Logger logger = lc.getLogger(loggerName);
		if (logger.iteratorForAppenders().hasNext()) {
			return logger;
		}

		RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<>();
		appender.setName(loggerName+"Appender");
		appender.setContext(lc);
		appender.setAppend(true);

		TimeBasedRollingPolicy rollingPolicy = new TimeBasedRollingPolicy<>();
		rollingPolicy.setFileNamePattern(SystemPropertyUtil.get("log.dir", "logs") +"/"+ loggerName + "/%d{yyyy-MM-dd}.log.gz");
		rollingPolicy.setParent(appender);
		rollingPolicy.setMaxHistory(60);
		rollingPolicy.setContext(lc);
		rollingPolicy.start();

		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setCharset(StandardCharsets.UTF_8);
		encoder.setPattern("[%d{yyyy-MM-dd HH:mm:ss.SSS}] %m%n");
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
	public String recordLoggerName() {
		return LogRecordManager.DEFAULT_LOGGER_RECORD_NAME;
	}

	@Override
	public <T extends Enum<T> & ILogRecordType<T>, L extends ILogRecordMsg<T>>  void send(L logRecordMsg) {
		Logger logger = loggers.computeIfAbsent(logRecordMsg.logType().getName(), this::createLogger);
		LoggingEvent le = new LoggingEvent(Logger.FQCN, logger, Level.INFO, logRecordMsg.getLogContentData(getter), null, null);
		le.setTimeStamp(logRecordMsg.createTime());
		logger.callAppenders(le);
	}
}
