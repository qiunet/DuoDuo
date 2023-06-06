package org.qiunet.cross.actor.data;

import org.qiunet.cross.rpc.IRpcRequest;
import org.qiunet.flash.handler.common.player.IPlayer;

/***
 * 跨服获取玩家数据的事务请求数据
 *
 * @author qiunet
 * 2020-10-28 12:03
 */
public class CrossDataRpcReq implements IRpcRequest, IPlayer {

	private String key;

	private long playerId;

	public static CrossDataRpcReq valueOf(String key, long playerId) {
		CrossDataRpcReq request = new CrossDataRpcReq();
		request.key = key;
		request.playerId = playerId;
		return request;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	@Override
	public long getId() {
		return playerId;
	}
}
