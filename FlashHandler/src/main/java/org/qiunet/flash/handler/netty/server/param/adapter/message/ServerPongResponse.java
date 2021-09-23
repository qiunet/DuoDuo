package org.qiunet.flash.handler.netty.server.param.adapter.message;

import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelData;

/***
 *
 * 服务器响应客户端ping信息
 * qiunet
 * 2021/9/23 17:17
 **/
@PbChannelData(ID = IProtocolId.System.CLIENT_PONG, desc = "服务器pong信息")
public class ServerPongResponse implements IpbChannelData {
}
