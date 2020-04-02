package org.qiunet.event.log.logger;

import org.qiunet.event.log.log.ILogEvent;

/***
 *
 * @author qiunet
 * 2020-03-25 10:33
 ***/
 class TcpLogger extends BaseLogger {

	TcpLogger(String loggerName) {
		super(loggerName);
	}

	@Override
	public void send(ILogEvent logEvent) {

	}
}
