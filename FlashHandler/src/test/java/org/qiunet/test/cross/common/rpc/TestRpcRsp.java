package org.qiunet.test.cross.common.rpc;

/***
 *
 * @author qiunet
 * 2020-10-23 20:54
 **/
public class TestRpcRsp {

	private long playerId;

	public static TestRpcRsp valueOf(long playerId) {
		TestRpcRsp response = new TestRpcRsp();
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
