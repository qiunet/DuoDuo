package org.qiunet.game.tests.protocol.proto.player;

import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelData;
import org.qiunet.game.tests.protocol.ProtocolId;

/***
 *
 *
 * qiunet
 * 2021/9/2 10:19
 **/
@PbChannelData(ID = ProtocolId.Player.UPGRADE_LV_REQ, desc = "升级请求")
public class UpgradeLevelRequest implements IpbChannelData {

	public static UpgradeLevelRequest valueOf(){
		UpgradeLevelRequest data = new UpgradeLevelRequest();
		return data;
	}
}
