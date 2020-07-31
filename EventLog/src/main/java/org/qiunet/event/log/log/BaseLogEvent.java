package org.qiunet.event.log.log;

import org.qiunet.event.log.enums.base.IEventLogType;

/***
 * 日志的基类.
 * @author qiunet
 * 2020-03-30 07:52
 **/
public abstract class BaseLogEvent<T extends Enum<T> & IEventLogType> implements ILogEvent<T> {
	protected T eventLogType;
	protected long createTime;

	protected BaseLogEvent(T eventLogType) {
		this.createTime = System.currentTimeMillis();
		this.eventLogType = eventLogType;
	}

	@Override
	public long createTime() {
		return createTime;
	}

	@Override
	public T logType() {
		return eventLogType;
	}
}
