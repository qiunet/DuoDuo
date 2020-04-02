package org.qiunet.event.log.log;

import org.qiunet.event.log.enums.base.IEventLogType;

/***
 *
 * @author qiunet
 * 2020-03-30 07:52
 **/
public abstract class BaseLogEvent implements ILogEvent {
	private IEventLogType eventLogType;

	public BaseLogEvent(IEventLogType eventLogType) {
		this.eventLogType = eventLogType;
	}

	@Override
	public IEventLogType logType() {
		return eventLogType;
	}
}
