package org.qiunet.log.record.msg;

import org.qiunet.log.record.enums.ILogRecordType;
import org.qiunet.utils.exceptions.CustomException;

import java.util.concurrent.atomic.AtomicBoolean;

/***
 * 日志的基类.
 *
 * @author qiunet
 * 2020-03-30 07:52
 **/
public abstract class LogRecordMsg<LogType extends Enum<LogType> & ILogRecordType> {
	private final AtomicBoolean logged = new AtomicBoolean();
	private final LogMsgBuilder logMsgBuilder;
	protected final LogType eventLogType;
	protected final long createTime;

	protected LogRecordMsg(LogType eventLogType) {
		this(eventLogType, "|");
	}

	protected LogRecordMsg(LogType eventLogType, String delimiter) {
		this(eventLogType, delimiter, "=");
	}

	protected LogRecordMsg(LogType eventLogType, String delimiter, String keyValDelimiter) {
		this.logMsgBuilder = new LogMsgBuilder(delimiter, keyValDelimiter);
		this.createTime = System.currentTimeMillis();
		this.eventLogType = eventLogType;
	}

	public String logMessage() {
		if (! logged.compareAndSet(false, true)) {
			throw new CustomException("Already output message!");
		}

		this.fillLogRecordMsg();
		return logMsgBuilder.toString();
	}

	protected void appendVal(Object val) {
		this.logMsgBuilder.add(val);
	}

	protected void appendVal(Object key, Object val) {
		this.logMsgBuilder.add(key, val);
	}

	/**
	 * 填充日志
	 */
	protected abstract void fillLogRecordMsg();

	public long createTime() {
		return createTime;
	}

	public LogType logType() {
		return eventLogType;
	}

	public void send() {
		LogRecordManager.instance.sendLog(this);
	}
}
