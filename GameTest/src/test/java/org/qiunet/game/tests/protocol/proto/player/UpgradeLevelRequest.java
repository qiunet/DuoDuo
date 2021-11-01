package org.qiunet.game.tests.protocol.proto.player;

import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.game.tests.protocol.ProtocolId;

/***
 *
 *
 * qiunet
 * 2021/9/2 10:19
 **/
@ChannelData(ID = ProtocolId.Player.UPGRADE_LV_REQ, desc = "升级请求")
public class UpgradeLevelRequest implements IChannelData {

	public static UpgradeLevelRequest valueOf(){
		UpgradeLevelRequest data = new UpgradeLevelRequest();
		return data;
	}
}
