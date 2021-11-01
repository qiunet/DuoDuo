package org.qiunet.game.tests.protocol.proto.player;

import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.game.tests.protocol.ProtocolId;

/***
 *
 *
 * qiunet
 * 2021/9/2 10:09
 **/
@ChannelData(ID = ProtocolId.Player.GET_EXP_REQ, desc = "获得经验请求")
public class GetExpRequest implements IChannelData {

	public static GetExpRequest valueOf(){
		return new GetExpRequest();
	}
}
