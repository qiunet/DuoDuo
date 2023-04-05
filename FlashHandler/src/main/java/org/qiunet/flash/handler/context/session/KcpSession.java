package org.qiunet.flash.handler.context.session;

import io.netty.channel.Channel;
import org.qiunet.flash.handler.netty.server.kcp.shakehands.message.KcpDisconnectPush;

/***
 * Kcp 通道的session
 *
 * @author qiunet
 * 2022/4/26 15:46
 */
public class KcpSession extends BaseChannelSession {

	public KcpSession(Channel channel) {
		this.addCloseListener("KcpDisconnectPush", (session, cause) -> {
			if (session.isActive()) {
				session.sendMessage(KcpDisconnectPush.getInstance());
			}
		});
		this.setChannel(channel);
	}
}
