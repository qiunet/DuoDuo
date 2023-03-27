package org.qiunet.flash.handler.netty.server.http.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
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
 * 17/12/1
 */
public class WebsocketServerHandler  extends SimpleChannelInboundHandler<MessageContent> {
	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();

	private final ServerBootStrapConfig config;

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// 因为通过http添加的Handler , 所以activate 已经没法调用了. 只能通过handlerShark Complete 事件搞定
		if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
			HttpHeaders headers = ((WebSocketServerProtocolHandler.HandshakeComplete) evt).requestHeaders();
			DSession iSession = new DSession(ctx.channel());
			ChannelUtil.bindSession(iSession, ctx.channel());

			iSession.attachObj(ServerConstants.HANDLER_TYPE_KEY, ServerConnType.WS);
			iSession.attachObj(ServerConstants.BOOTSTRAP_CONFIG_KEY, config);
			iSession.attachObj(ServerConstants.HTTP_WS_HEADER_KEY, headers);
			config.getStartupContext().buildMessageActor(iSession);
		}
		super.userEventTriggered(ctx, evt);
	}

	public WebsocketServerHandler (ServerBootStrapConfig config) {
		this.config = config;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MessageContent content) throws Exception {
		ChannelUtil.channelRead(ctx.channel(), config, content);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ChannelUtil.cause(config.getStartupContext(), ctx.channel(), cause);
	}
}
