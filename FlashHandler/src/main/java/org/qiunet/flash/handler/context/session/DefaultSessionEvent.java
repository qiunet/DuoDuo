package org.qiunet.flash.handler.context.session;

import io.netty.channel.ChannelHandlerContext;
import org.qiunet.flash.handler.common.enums.HandlerType;
import org.qiunet.flash.handler.context.request.IRequest;
import org.qiunet.utils.logger.LoggerManager;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.logger.log.QLogger;

/**
 * Created by qiunet.
 * 17/11/27
 */
public class DefaultSessionEvent implements ISessionEvent {
	protected QLogger logger = LoggerManager.getLogger(LoggerType.FLASH_HANDLER);
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
		sessionManager.getSession(ctx.channel()).setLastPackageTimeStamp();
	}
}
