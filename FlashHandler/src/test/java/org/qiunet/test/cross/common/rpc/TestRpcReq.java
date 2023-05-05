package org.qiunet.test.cross.common.rpc;

import org.qiunet.cross.rpc.IRpcRequest;
import org.qiunet.flash.handler.common.player.IPlayer;

/***
 *
 * @author qiunet
 * 2020-10-23 20:54
 **/
public class TestRpcReq implements IRpcRequest, IPlayer {

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

	@Override
	public long getId() {
		return getPlayerId();
	}
}
