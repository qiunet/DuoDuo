package org.qiunet.event.log.log;

import org.qiunet.event.log.LogRecord;
import org.qiunet.event.log.enums.base.IEventLogType;


/***
 *
 * @author qiunet
 * 2020-03-29 10:07
 **/
public interface ILogEvent {
	/**
	 * 日志的类型
	 * @return
	 */
	IEventLogType logType();
	/**
	 * 生成发送的消息
	 * @return
	 */
	String logMessage();
	/**
	 * 日志生成时间 13位时间戳
	 * @return
	 */
	long createTime();
	/**
	 * 发送日志
	 */
	default void send() {
		LogRecord.sendLog(this);
	}
}
