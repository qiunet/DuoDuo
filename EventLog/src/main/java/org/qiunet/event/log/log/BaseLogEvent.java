package org.qiunet.event.log.log;

import org.qiunet.event.log.enums.base.IEventLogType;

/***
 *
 * @author qiunet
 * 2020-03-30 07:52
 **/
public abstract class BaseLogEvent implements ILogEvent {
	private IEventLogType eventLogType;
	private long createTime;

	public BaseLogEvent(IEventLogType eventLogType) {
		this.createTime = System.currentTimeMillis();
		this.eventLogType = eventLogType;
	}

	@Override
	public long createTime() {
		return createTime;
	}

	@Override
	public IEventLogType logType() {
		return eventLogType;
	}
}
