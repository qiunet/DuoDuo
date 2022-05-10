package org.qiunet.flash.handler.netty.server.kcp.shakehands.message;

import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

/***
 *
 * @author qiunet
 * 2022/5/10 14:39
 */
@ChannelData(ID = IProtocolId.System.KCP_DISCONNECT_PUSH, desc = "")
public class KcpDisconnectPush implements IChannelData {

	private static final KcpDisconnectPush instance = new KcpDisconnectPush();

	public static KcpDisconnectPush getInstance() {
		return instance;
	}
}
