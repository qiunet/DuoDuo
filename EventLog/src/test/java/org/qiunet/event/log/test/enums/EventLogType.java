package org.qiunet.event.log.test.enums;

import org.qiunet.event.log.enums.RecordModel;
import org.qiunet.event.log.enums.base.IEventLogType;

/***
 *
 *
 * @author qiunet
 * 2020-04-02 15:26
 ***/
public enum EventLogType implements IEventLogType {
	/**金币日志**/
	M1(RecordModel.LOCAL),
	;
	private RecordModel model;

	EventLogType(RecordModel model) {
		this.model = model;
	}

	@Override
	public String getLoggerName() {
		return name().toLowerCase();
	}

	@Override
	public RecordModel recordModel() {
		return model;
	}
}
