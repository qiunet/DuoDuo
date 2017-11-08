package org.qiunet.utils.logger.log;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

/**
 * Created by qiunet.
 * 17/11/7
 */
public class Log4j2Logger implements QLogger{

	private Logger logger;

	public Log4j2Logger(Logger logger) {
		this.logger = logger;
	}

	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	@Override
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	@Override
	public void debug(Object msg) {
		this.logger.debug(msg);
	}

	@Override
	public void debug(Object msg, Throwable t) {
		this.logger.debug(msg, t);
	}

	@Override
	public void info(Object msg) {
		this.logger.info(msg);
	}

	@Override
	public void info(Object msg, Throwable t) {
		this.logger.info(msg, t);
	}

	@Override
	public void warn(Object msg) {
		this.logger.warn(msg);
	}

	@Override
	public void warn(Object msg, Throwable t) {
		this.logger.warn(msg , t);
	}

	@Override
	public void error(Object msg) {
		this.logger.error(msg);
	}

	@Override
	public void error(Object msg, Throwable t) {
		this.logger.error(msg, t);
	}

	@Override
	public boolean isEnabledFor(String level) {
		return logger.isEnabled(Level.getLevel(level));
	}
}
