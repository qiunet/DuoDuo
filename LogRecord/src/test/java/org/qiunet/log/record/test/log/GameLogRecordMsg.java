package org.qiunet.log.record.test.log;

import org.qiunet.log.record.msg.StringLogRecordMsg;
import org.qiunet.log.record.test.enums.LogRecordType;

/***
 *
 *
 * @author qiunet
 * 2020-04-02 15:35
 ***/
public abstract class GameLogRecordMsg extends StringLogRecordMsg<LogRecordType> {

	public GameLogRecordMsg(LogRecordType type) {
		super(type, "/");
	}
}
