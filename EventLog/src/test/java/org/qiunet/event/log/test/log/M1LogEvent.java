package org.qiunet.event.log.test.log;

import org.qiunet.event.log.test.enums.EventLogType;

import java.util.StringJoiner;

/***
 *
 *
 * @author qiunet
 * 2020-04-02 15:33
 ***/
public class M1LogEvent extends AbstractLogEvent {
	private long playerId;
	private long currM1;
	private int m1;
	private String operate;

	public M1LogEvent(long playerId, long currM1, int m1, String operate) {
		super(EventLogType.M1);
		this.playerId = playerId;
		this.currM1 = currM1;
		this.m1 = m1;
		this.operate = operate;
	}

	@Override
	public String logMessage() {
		StringJoiner sj = new StringJoiner("|");
		sj.add(String.valueOf(playerId))
			.add(String.valueOf(currM1))
			.add(String.valueOf(m1))
			.add(operate);
		return sj.toString();
	}
}
