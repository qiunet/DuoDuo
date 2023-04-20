package org.qiunet.flash.handler.netty.server.tcp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;


/**
 * Created by qiunet.
 * 17/8/13
 */
public class TcpServerHandler extends ChannelInboundHandlerAdapter {
	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		DSession session = new DSession(ctx.channel());
		ChannelUtil.bindSession(session, ctx.channel());

		logger.debug("Tcp session {} active!", session);
		session.attachObj(ServerConstants.MESSAGE_ACTOR_KEY, new PlayerActor(session));
		session.attachObj(ServerConstants.HANDLER_TYPE_KEY, ServerConnType.TCP);
		ctx.fireChannelActive();
	}
}
