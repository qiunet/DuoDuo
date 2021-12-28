package org.qiunet.event.log.log;

import org.qiunet.event.log.enums.base.IEventLogType;
import org.qiunet.utils.exceptions.CustomException;

import java.util.concurrent.atomic.AtomicBoolean;

/***
 * 日志的基类.
 * @author qiunet
 * 2020-03-30 07:52
 **/
public abstract class BaseLogEvent<T extends Enum<T> & IEventLogType> implements ILogEvent<T> {
	private final AtomicBoolean logged = new AtomicBoolean();
	private final LogMessageBuilder logMessageBuilder;
	protected final T eventLogType;
	protected final long createTime;

	protected BaseLogEvent(T eventLogType) {
		this(eventLogType, "|");
	}

	protected BaseLogEvent(T eventLogType, String delimiter) {
		this(eventLogType, delimiter, "=");
	}

	protected BaseLogEvent(T eventLogType, String delimiter, String keyValDelimiter) {
		this.logMessageBuilder = new LogMessageBuilder(delimiter, keyValDelimiter);
		this.createTime = System.currentTimeMillis();
		this.eventLogType = eventLogType;
	}

	@Override
	public String logMessage() {
		if (! logged.compareAndSet(false, true)) {
			throw new CustomException("Already output message!");
		}
		this.appendLog(logMessageBuilder);
		return logMessageBuilder.toString();
	}

	protected abstract void appendLog(LogMessageBuilder builder);

	@Override
	public long createTime() {
		return createTime;
	}

	@Override
	public T logType() {
		return eventLogType;
	}
}
