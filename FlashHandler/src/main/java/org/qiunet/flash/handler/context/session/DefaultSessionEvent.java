package org.qiunet.flash.handler.context.session;

import io.netty.channel.ChannelHandlerContext;
import org.qiunet.utils.logger.LoggerManager;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.logger.log.QLogger;

/**
 * Created by qiunet.
 * 17/11/27
 */
public class DefaultSessionEvent implements ISessionEvent {
	private QLogger logger = LoggerManager.getLogger(LoggerType.FLASH_HANDLER);

	@Override
	public void sessionRegistered(ChannelHandlerContext ctx) {
		logger.info("DefaultSessionEvent called sessionRegistered");
	}

	@Override
	public void sessionUnregistered(ChannelHandlerContext ctx) {
		logger.info("DefaultSessionEvent called sessionUnregistered");
	}
}
