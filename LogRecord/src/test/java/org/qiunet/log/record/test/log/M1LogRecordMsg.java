package org.qiunet.log.record.test.log;

import org.qiunet.log.record.test.enums.LogRecordType;

/***
 *
 *
 * @author qiunet
 * 2020-04-02 15:33
 ***/
public class M1LogRecordMsg extends GameLogRecordMsg {
	private final long playerId;
	private final String operate;
	private final long currM1;
	private final int m1;

	public M1LogRecordMsg(long playerId, long currM1, int m1, String operate) {
		super(LogRecordType.MONEY1);
		this.playerId = playerId;
		this.currM1 = currM1;
		this.m1 = m1;
		this.operate = operate;
	}

	@Override
	protected void fillLogRecordMsg() {
		this.appendVal(String.valueOf(playerId));
		this.appendVal(String.valueOf(currM1));
		this.appendVal(String.valueOf(m1));
		this.appendVal(operate);
	}
}
