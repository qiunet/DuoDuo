package org.qiunet.flash.handler.netty.server.tcp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.server.config.ServerBootStrapConfig;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;


/**
 * Created by qiunet.
 * 17/8/13
 */
public class TcpServerHandler extends SimpleChannelInboundHandler<MessageContent> {
	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	private final ServerBootStrapConfig config;

	public TcpServerHandler(ServerBootStrapConfig config) {
		this.config = config;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.channel().attr(ServerConstants.HANDLER_TYPE_KEY).set(ServerConnType.TCP);
		DSession session = new DSession(ctx.channel());

		ChannelUtil.bindSession(session);
		logger.debug("Tcp session {} active!", session);
		ctx.channel().attr(ServerConstants.BOOTSTRAP_CONFIG_KEY).set(config);
		ctx.channel().attr(ServerConstants.MESSAGE_ACTOR_KEY).set(config.getStartupContext().buildMessageActor(session));
		ctx.fireChannelActive();
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, MessageContent content) throws Exception {
		if (ChannelUtil.handlerPing(ctx.channel(), content)) {
			return;
		}
		ChannelUtil.channelRead(ctx.channel(), config, content);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ChannelUtil.cause(config.getStartupContext(), ctx.channel(), cause);
	}
}
