package org.qiunet.game.tests.protocol.proto.login;

import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

import java.util.List;

import static org.qiunet.game.tests.protocol.ProtocolId.Login.PLAYER_INDEX_RSP;


/***
 *
 *
 * @author qiunet
 * 2020-09-23 10:22
 */
@ChannelData(ID = PLAYER_INDEX_RSP, desc = "玩家首页响应")
public class PlayerIndexResponse extends IChannelData {

	private List<Item> items;

	private PlayerData PlayerData;

	public static PlayerIndexResponse valueOf(PlayerData PlayerData, List<Item> items) {
		PlayerIndexResponse response = new PlayerIndexResponse();
		response.PlayerData = PlayerData;
		response.items = items;
		return response;
	}

	public PlayerData getPlayerData() {
		return PlayerData;
	}

	public List<Item> getItems() {
		return items;
	}
}
