package org.qiunet.event.log.logger;

import org.qiunet.event.log.log.ILogEvent;

/***
 *
 * @author qiunet
 * 2020-03-25 10:33
 ***/
 enum  TcpLogger implements ILogger {
	instance;

	@Override
	public String loggerName() {
		return "TcpLogger";
	}

	@Override
	public void send(ILogEvent logEvent) {

	}
}
