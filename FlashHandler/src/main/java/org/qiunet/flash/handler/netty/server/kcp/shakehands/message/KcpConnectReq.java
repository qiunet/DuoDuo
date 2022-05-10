package org.qiunet.flash.handler.netty.server.kcp.shakehands.message;

import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

/***
 *
 * @author qiunet
 * 2022/5/10 14:39
 */
@ChannelData(ID = IProtocolId.System.KCP_CONNECT_REQ, desc = "客户端KCP连接请求")
public class KcpConnectReq implements IChannelData {
}
