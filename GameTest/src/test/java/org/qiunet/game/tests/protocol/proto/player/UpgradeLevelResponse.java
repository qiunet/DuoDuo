package org.qiunet.game.tests.protocol.proto.player;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelData;
import org.qiunet.game.tests.protocol.ProtocolId;

/***
 *
 *
 * qiunet
 * 2021/9/2 10:19
 **/
@PbChannelData(ID = ProtocolId.Player.UPGRADE_LV_RSP, desc = "升级响应")
public class UpgradeLevelResponse implements IpbChannelData {
	@Protobuf(description = "当前等级")
	private int currLv;

	public static UpgradeLevelResponse valueOf(int currLv){
		UpgradeLevelResponse data = new UpgradeLevelResponse();
		data.currLv = currLv;
		return data;
	}

	public int getCurrLv() {
		return currLv;
	}
}
