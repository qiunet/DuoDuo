package org.qiunet.game.tests.protocol.proto;

import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelData;
import org.qiunet.game.tests.protocol.ProtocolId;

/***
 * 获取随机名称
 *
 * qiunet
 * 2021/8/4 11:07
 **/
@PbChannelData(ID = ProtocolId.Login.RANDOM_NAME_REQ, desc = "获取随机名称请求")
public class RandomNameRequest implements IpbChannelData {

	public static RandomNameRequest valueOf() {
		return new RandomNameRequest();
	}
}
