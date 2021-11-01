package org.qiunet.cross.test.proto.req;

import org.qiunet.cross.test.handler.ProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

/***
 *
 *
 * @author qiunet
 * 2020-10-23 09:55
 */
@ChannelData(ID = ProtocolId.Player.PLAYER_LOGIN, desc = "登录请求")
public class LoginRequest implements IChannelData {

	private long playerId;

	public static LoginRequest valueOf(long playerId) {
		LoginRequest request = new LoginRequest();
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
