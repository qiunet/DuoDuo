package org.qiunet.game.tests.protocol.proto;

import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelData;

import java.util.List;

import static org.qiunet.game.tests.protocol.ProtocolId.Login.PLAYER_INDEX_RSP;


/***
 *
 *
 * @author qiunet
 * 2020-09-23 10:22
 */
@PbChannelData(ID = PLAYER_INDEX_RSP, desc = "玩家首页响应")
public class PlayerIndexResponse implements IpbChannelData {
	private long playerId;

	private List<Item> items;

	public static PlayerIndexResponse valueOf(long playerId, List<Item> items) {
		PlayerIndexResponse response = new PlayerIndexResponse();
		response.playerId = playerId;
		response.items = items;
		return response;
	}

	public long getPlayerId() {
		return playerId;
	}

	public List<Item> getItems() {
		return items;
	}
}
