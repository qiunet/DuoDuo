package org.qiunet.game.tests.protocol.proto.player;

import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelData;
import org.qiunet.game.tests.protocol.ProtocolId;

/***
 *
 *
 * qiunet
 * 2021/9/2 10:09
 **/
@PbChannelData(ID = ProtocolId.Player.GET_EXP_REQ, desc = "获得经验请求")
public class GetExpRequest implements IpbChannelData {

	public static GetExpRequest valueOf(){
		return new GetExpRequest();
	}
}
