package org.qiunet.game.tests.protocol.proto.login;

import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.game.tests.protocol.ProtocolId;

/***
 * 获取随机名称
 *
 * qiunet
 * 2021/8/4 11:07
 **/
@ChannelData(ID = ProtocolId.Login.RANDOM_NAME_REQ, desc = "获取随机名称请求")
public class RandomNameRequest implements IChannelData {

	public static RandomNameRequest valueOf() {
		return new RandomNameRequest();
	}
}
