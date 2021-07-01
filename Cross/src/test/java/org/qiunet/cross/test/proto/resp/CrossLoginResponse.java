package org.qiunet.cross.test.proto.resp;

import org.qiunet.cross.test.handler.ProtocolId;
import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelData;

/***
 *
 *
 * @author qiunet
 * 2020-10-26 12:39
 */
@PbChannelData(ID = ProtocolId.Player.CROSS_PLAYER_LOGIN_SUCCESS, desc = "跨服登录成功")
public class CrossLoginResponse implements IpbChannelData {
	private String playerName;

	public static CrossLoginResponse valueOf(String playerName) {
		CrossLoginResponse response = new CrossLoginResponse();
		response.playerName = playerName;
		return response;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
}
