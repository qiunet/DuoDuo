package org.qiunet.cross.test.proto.req;

import org.qiunet.cross.test.handler.ProtocolId;
import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelData;

/***
 *
 *
 * @author qiunet
 * 2020-10-23 09:55
 */
@PbChannelData(ID = ProtocolId.Player.PLAYER_LOGIN, desc = "登录请求")
public class LoginRequest implements IpbChannelData {

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
