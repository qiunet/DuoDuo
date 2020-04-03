package org.qiunet.event.log.enums;

import org.qiunet.event.log.logger.ILogger;
import org.qiunet.event.log.logger.LoggerChoice;

/***
 *
 *
 * @author qiunet
 * 2020-03-25 09:53
 ***/
public enum RecordModel {
	/**
	 * 本地保存
	 */
	LOCAL,
	/**
	 * tcp 发到事件日志保存平台.
	 * 重要日志一般使用tcp
	 */
	TCP,
	/***
	 * udp发到事件日志保存平台
	 * 普通日志使用udp, 有一定丢失可能.
	 */
	UDP,
	;

	public ILogger getLogger(String loggerName){
		return LoggerChoice.getLogger(this, loggerName);
	}
}
