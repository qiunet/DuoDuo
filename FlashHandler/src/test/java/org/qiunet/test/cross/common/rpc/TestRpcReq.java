package org.qiunet.test.cross.common.rpc;

import org.qiunet.cross.rpc.IRpcRequest;

/***
 *
 * @author qiunet
 * 2020-10-23 20:54
 **/
public class TestRpcReq implements IRpcRequest {

	private long playerId;

	public static TestRpcReq valueOf(long playerId) {
		TestRpcReq request = new TestRpcReq();
		request.playerId = playerId;
		return request;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public long getId() {
		return getPlayerId();
	}
}
