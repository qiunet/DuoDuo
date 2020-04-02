package org.qiunet.event.log.logger;

import org.qiunet.event.log.log.ILogEvent;

/***
 * 暂时不实现
 *
 * @author qiunet
 * 2020-03-25 10:33
 ***/
 class Log4jLogger extends BaseLogger {

 	Log4jLogger(String loggerName) {
		super(loggerName);
	}

	@Override
	public void send(ILogEvent logEvent) {

	}
}
