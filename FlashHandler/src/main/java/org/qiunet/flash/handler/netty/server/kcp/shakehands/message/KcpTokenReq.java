package org.qiunet.flash.handler.netty.server.kcp.shakehands.message;

import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

/***
 *
 * @author qiunet
 * 2022/4/27 11:06
 */
@ChannelData(ID = IProtocolId.System.KCP_TOKEN_REQ, desc = "kcp的token申请请求")
public class KcpTokenReq implements IChannelData {

}
