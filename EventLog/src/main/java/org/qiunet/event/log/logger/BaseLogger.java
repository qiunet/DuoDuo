package org.qiunet.event.log.logger;

/***
 *
 *
 * @author qiunet
 * 2020-04-02 11:12
 ***/
abstract class BaseLogger implements ILogger {
	private String loggerName;

	public BaseLogger(String loggerName) {
		this.loggerName = loggerName;
	}

	@Override
	public String loggerName() {
		return loggerName;
	}
}
