package org.qiunet.log.record.logger;

import org.qiunet.log.record.enums.ILogRecordType;
import org.qiunet.log.record.msg.LogRecordMsg;

/***
 *
 * 自己的一个通用logger接口.
 * 子类可以是TcpRecordLogger UdpRecordLogger LogBackRecordLogger
 * @author qiunet
 * 2020-03-25 10:36
 ***/
public interface IRecordLogger {
	/**
	 * logger 的名称
	 * @return
	 */
	 String loggerName();

	/**
	 * 记录日志
	 * @param logRecordMsg
	 */
	 <T extends Enum<T> & ILogRecordType, L extends LogRecordMsg<T>> void send(L logRecordMsg);
}
