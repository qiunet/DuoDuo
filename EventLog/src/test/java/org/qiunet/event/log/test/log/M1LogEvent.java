package org.qiunet.event.log.test.log;

import org.qiunet.event.log.log.LogMessageBuilder;
import org.qiunet.event.log.test.enums.EventLogType;

/***
 *
 *
 * @author qiunet
 * 2020-04-02 15:33
 ***/
public class M1LogEvent extends GameLogEvent {
	private long playerId;
	private long currM1;
	private int m1;
	private String operate;

	public M1LogEvent(long playerId, long currM1, int m1, String operate) {
		super(EventLogType.MONEY1);
		this.playerId = playerId;
		this.currM1 = currM1;
		this.m1 = m1;
		this.operate = operate;
	}

	@Override
	protected void appendLog(LogMessageBuilder builder) {
		builder.add(String.valueOf(playerId))
			.add(String.valueOf(currM1))
			.add(String.valueOf(m1))
			.add(operate);
	}
}
