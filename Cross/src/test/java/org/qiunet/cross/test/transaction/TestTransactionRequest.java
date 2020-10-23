package org.qiunet.cross.test.transaction;

import org.qiunet.cross.transaction.BaseTransactionRequest;

/***
 *
 * @author qiunet
 * 2020-10-23 20:54
 **/
public class TestTransactionRequest extends BaseTransactionRequest {

	private long playerId;

	public static TestTransactionRequest valueOf(long playerId) {
		TestTransactionRequest request = new TestTransactionRequest();
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
