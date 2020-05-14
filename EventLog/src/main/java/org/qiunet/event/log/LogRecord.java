package org.qiunet.event.log;

import com.google.common.base.Preconditions;
import org.qiunet.event.log.enums.base.IEventLogType;
import org.qiunet.event.log.log.ILogEvent;
import org.qiunet.event.log.logger.LoggerChoice;

/***
 *
 *
 * @author qiunet
 * 2020-03-25 09:51
 ***/
public final class LogRecord {
	/**
	 * 发送日志
	 * @param log
	 */
	public static void sendLog(ILogEvent log){
		Preconditions.checkNotNull(log);
		LoggerChoice.getLogger((IEventLogType) log.logType()).send(log);
	}
}
