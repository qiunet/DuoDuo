package org.qiunet.flash.handler.netty.server.param.adapter.message;

import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

/***
 *
 * 请求ping信息
 * qiunet
 * 2021/9/23 17:17
 **/
@ChannelData(ID = IProtocolId.System.CLIENT_PING, desc = "ping信息")
public enum ClientPingRequest implements IChannelData {
	instance;
}
