package org.qiunet.event.log.test.log;

import org.qiunet.event.log.log.BaseLogEvent;
import org.qiunet.event.log.test.enums.EventLogType;

/***
 *
 *
 * @author qiunet
 * 2020-04-02 15:35
 ***/
public abstract class AbstractLogEvent extends BaseLogEvent<EventLogType> {

	public AbstractLogEvent(EventLogType type) {
		super(type);
	}
}
