package org.qiunet.flash.handler.context.session;

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
	public void sessionRegistered(ChannelHandlerContext ctx) {
		sessionManager.addSession(new DefaultPlayerSession(ctx));
	}

	@Override
	public void sessionUnregistered(ChannelHandlerContext ctx) {
		sessionManager.removeSession(ctx.channel());
	}

	@Override
	public void sessionReceived(ChannelHandlerContext ctx, HandlerType type, IRequest msg) {
		ISession session = sessionManager.getSession(ctx.channel());
		if (session == null) {
			logger.error("Session is close in server. It is not accept any message. ");
			ctx.close();
			return;
		}
		session.setLastPackageTimeStamp();
	}
}
