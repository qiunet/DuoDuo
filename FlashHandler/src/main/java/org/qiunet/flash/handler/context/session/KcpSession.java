package org.qiunet.flash.handler.context.session;

import io.netty.channel.Channel;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.session.future.IDSessionFuture;
import org.qiunet.flash.handler.netty.server.kcp.shakehands.message.KcpDisconnectPush;
import org.qiunet.utils.exceptions.CustomException;

/***
 *
 * @author qiunet
 * 2022/4/26 15:46
 */
public class KcpSession extends BaseSession {

	public KcpSession(Channel channel) {
		this.addCloseListener("KcpDisconnectPush", (session, cause) -> {
			if (session.isActive()) {
				session.sendMessage(KcpDisconnectPush.getInstance());
			}
		});
		this.setChannel(channel);
	}

	@Override
	public boolean isKcpSessionPrepare() {
		return true;
	}

	@Override
	public void flush() {}

	@Override
	public void bindKcpSession(KcpSession kcpSession) {
		throw new CustomException("Not Support!");
	}

	@Override
	public KcpSession getKcpSession() {
		return this;
	}

	@Override
	public IDSessionFuture sendKcpMessage(IChannelMessage<?> message) {
		return this.sendMessage(message, true);
	}
}
