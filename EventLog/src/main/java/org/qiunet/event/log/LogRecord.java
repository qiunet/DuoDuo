package org.qiunet.event.log;

import com.google.common.base.Preconditions;
import org.qiunet.event.log.enums.RecordModel;
import org.qiunet.event.log.log.ILogEvent;

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

		String logMessage = log.logMessage();
		RecordModel recordModel = log.logType().recordModel();
		recordModel.getLogger(log.logType().getLoggerName()).send(logMessage);
	}
}
