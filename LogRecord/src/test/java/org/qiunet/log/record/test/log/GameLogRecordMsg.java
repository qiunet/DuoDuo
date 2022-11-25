package org.qiunet.log.record.test.log;

import org.qiunet.log.record.msg.LogRecordMsg;
import org.qiunet.log.record.test.enums.LogRecordType;

/***
 *
 *
 * @author qiunet
 * 2020-04-02 15:35
 ***/
public abstract class GameLogRecordMsg extends LogRecordMsg<LogRecordType> {

	public GameLogRecordMsg(LogRecordType type) {
		super(type);
	}
}
