package org.qiunet.cross.test.transaction;

import org.qiunet.cross.transaction.BaseTransactionResponse;

/***
 *
 * @author qiunet
 * 2020-10-23 20:54
 **/
public class TestTransactionResponse extends BaseTransactionResponse {

	private long playerId;

	public static TestTransactionResponse valueOf(long playerId) {
		TestTransactionResponse response = new TestTransactionResponse();
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
