package org.qiunet.cross.test.proto.resp;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.cross.test.handler.ProtocolId;
import org.qiunet.flash.handler.context.request.data.pb.IpbResponseData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelDataID;

/***
 *
 *
 * @author qiunet
 * 2020-10-26 12:39
 */
@ProtobufClass(description = "跨服登录成功")
@PbChannelDataID(ProtocolId.Player.CROSS_PLAYER_LOGIN_SUCCESS)
public class CrossLoginResponse implements IpbResponseData {
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
