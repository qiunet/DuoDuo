package org.qiunet.event.log.logger;

import org.qiunet.event.log.log.ILogEvent;

/***
 *
 * 自己的一个通用logger接口.
 * 子类可以是TcpLogger UdpLogger Log4JLogger LogBackLogger
 * @author qiunet
 * 2020-03-25 10:36
 ***/
public interface ILogger {
	/**
	 * logger 的名称
	 * @return
	 */
	 String loggerName();

	/**
	 * 记录日志
	 * @param logEvent
	 */
	 void send(ILogEvent logEvent);
}
