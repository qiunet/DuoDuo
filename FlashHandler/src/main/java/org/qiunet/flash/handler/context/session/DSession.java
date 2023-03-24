package org.qiunet.flash.handler.context.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.Attribute;
import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.session.kcp.IKcpSessionHolder;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.utils.exceptions.CustomException;

/**
 * Session 的 父类
 * Created by qiunet.
 * 17/11/26
 */
public class DSession extends BaseChannelSession implements IKcpSessionHolder {
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
		Attribute<ServerConnType> attr = this.channel.attr(ServerConstants.HANDLER_TYPE_KEY);
		if (attr.get() != ServerConnType.TCP && attr.get() != ServerConnType.WS) {
			throw new CustomException("Not support!");
		}
		if (this.kcpSession != null) {
			this.closeListeners.remove("CloseKcpSession");
			this.kcpSession.close(CloseCause.LOGIN_REPEATED);
		}
		this.kcpSession = kcpSession;
		this.addCloseListener("CloseKcpSession", (session, cause) -> {
			logger.debug("Close kcp session!");
			this.kcpSession.close(cause);
		});
	}

	@Override
	public ChannelFuture sendKcpMessage(IChannelMessage<?> message, boolean flush) {
		if (this.getKcpSession() == null || ! this.getKcpSession().isActive()) {
			logger.warn("Not bind kcp session or session inactive!");
			return this.sendMessage(message, flush);
		}
		return this.getKcpSession().sendMessage(message, flush);
	}

	public KcpSession getKcpSession() {
		return kcpSession;
	}
}
