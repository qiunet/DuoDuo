package org.qiunet.game.tests.protocol.proto.player;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.game.tests.protocol.ProtocolId;

/***
 *
 *
 * qiunet
 * 2021/9/2 10:09
 **/
@ChannelData(ID = ProtocolId.Player.GET_EXP_RSP, desc = "获得经验响应")
public class GetExpResponse extends IChannelData {
	@Protobuf(description = "获得的经验")
	private int addExp;

	public static GetExpResponse valueOf(int addExp){
		GetExpResponse response =  new GetExpResponse();
		response.addExp = addExp;
		return response;
	}

	public int getAddExp() {
		return addExp;
	}
}
