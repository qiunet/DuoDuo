package org.qiunet.flash.handler.context.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.qiunet.flash.handler.common.enums.HandlerType;
import org.qiunet.flash.handler.context.request.IRequest;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by qiunet.
 * 17/11/27
 */
public class DefaultSessionEvent implements ISessionEvent {
	protected Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);
	protected SessionManager sessionManager = SessionManager.getInstance();
	@Override
	public void sessionRegistered(Channel channel) {
//		sessionManager.addSession(new DefaultPlayerSession(channel));
	}

	@Override
	public void sessionUnregistered(Channel channel) {
//		sessionManager.removeSession(channel);
	}

	@Override
	public void sessionReceived(Channel channel, HandlerType type, IRequest msg) {
		ISession session = sessionManager.getSession(channel);
		if (session == null) {
			this.sessionRegistered(channel);
			session = sessionManager.getSession(channel);
		}
	}
}
