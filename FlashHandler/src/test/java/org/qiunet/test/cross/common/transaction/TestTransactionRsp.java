package org.qiunet.test.cross.common.transaction;

import org.qiunet.cross.transaction.ITransactionRsp;

/***
 *
 * @author qiunet
 * 2020-10-23 20:54
 **/
public class TestTransactionRsp implements ITransactionRsp {

	private long playerId;

	public static TestTransactionRsp valueOf(long playerId) {
		TestTransactionRsp response = new TestTransactionRsp();
		response.playerId = playerId;
		return response;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
}
