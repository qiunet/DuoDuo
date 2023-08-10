package org.qiunet.flash.handler.netty.server.kcp.shakehands.message;

import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

/***
 *
 * @author qiunet
 * 2022/4/27 11:27
 */
@ChannelData(ID = IProtocolId.System.KCP_BIND_AUTH_FIRST_PUSH, desc = "KCP需要先鉴权推送", kcp = true)
public class KcpBindAuthFirstPush extends IChannelData {
	public static KcpBindAuthFirstPush valueOf() {
		return new KcpBindAuthFirstPush();
	}
}
