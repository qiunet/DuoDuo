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
abstract class LogRecordMsg<LogType extends Enum<LogType> & ILogRecordType<LogType>> implements ILogRecordMsg<LogType>{
	private final AtomicBoolean logged = new AtomicBoolean();
	protected final LogType eventLogType;
	protected final long createTime;

	protected LogRecordMsg(LogType eventLogType) {
		this.createTime = System.currentTimeMillis();
		this.eventLogType = eventLogType;
	}

	@Override
	public Object getData() {
		if (! logged.compareAndSet(false, true)) {
			throw new CustomException("Already output message!");
		}

		this.fillLogRecordMsg();
		return getData0();
	}

	/**
	 * 填充日志
	 */
	protected abstract void fillLogRecordMsg();
	protected abstract Object getData0();
	@Override
	public long createTime() {
		return createTime;
	}
	@Override
	public LogType logType() {
		return eventLogType;
	}
}
