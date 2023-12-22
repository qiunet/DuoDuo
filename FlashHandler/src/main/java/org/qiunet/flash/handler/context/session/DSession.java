package org.qiunet.flash.handler.context.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.session.kcp.IKcpSessionManager;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;

/**
 * Player 连 服务器使用的 Session
 * Created by qiunet.
 * 17/11/26
 */
public class DSession extends BaseChannelSession implements IKcpSessionManager {
	/**
	 * 绑定的kcp session
	 */
	protected KcpSession kcpSession;

	protected DSession(){}

	public DSession(Channel channel) {
		this.setChannel(channel);
	}

	@Override
	public void bindKcpSession(KcpSession kcpSession) {
		if (this.kcpSession != null) {
			this.kcpSession.close(CloseCause.LOGIN_REPEATED);
		}

		this.closeListeners.put("CloseKcpSession", (s1, c) -> {
			((DSession) s1).kcpSession.close(c);
		});

		kcpSession.setDependOnTcp();
		this.kcpSession = kcpSession;
	}

	@Override
	public void close(CloseCause cause) {
		if (kcpSession != null) {
			this.kcpSession.close(cause);
		}
		super.close(cause);
	}

	@Override
	public ChannelFuture sendKcpMessage(IChannelMessage<?> message, boolean flush) {
		if (this.getKcpSession() == null || ! this.getKcpSession().isActive()) {
			logger.warn("Not bind kcp session or session inactive!");
			return this.sendMessage(message, flush);
		}
		return IKcpSessionManager.super.sendKcpMessage(message, flush);
	}

	public KcpSession getKcpSession() {
		return kcpSession;
	}
}
