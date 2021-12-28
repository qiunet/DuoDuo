package org.qiunet.test.cross.common.transaction;

import org.qiunet.cross.transaction.ITransactionReq;

/***
 *
 * @author qiunet
 * 2020-10-23 20:54
 **/
public class TestTransactionReq implements ITransactionReq {

	private long playerId;

	public static TestTransactionReq valueOf(long playerId) {
		TestTransactionReq request = new TestTransactionReq();
		request.playerId = playerId;
		return request;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
}
