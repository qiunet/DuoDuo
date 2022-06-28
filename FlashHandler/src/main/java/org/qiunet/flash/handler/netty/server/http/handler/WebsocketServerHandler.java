package org.qiunet.flash.handler.netty.server.http.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

/**
 * Created by qiunet.
 * 17/12/1
 */
public class WebsocketServerHandler  extends SimpleChannelInboundHandler<MessageContent> {
	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();

	private final HttpBootstrapParams params;


	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// 因为通过http添加的Handler , 所以activate 已经没法调用了. 只能通过handlerShark Complete 事件搞定
		if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
			HttpHeaders headers = ((WebSocketServerProtocolHandler.HandshakeComplete) evt).requestHeaders();
			ctx.channel().attr(ServerConstants.HANDLER_TYPE_KEY).set(ServerConnType.WS);

			DSession iSession = new DSession(ctx.channel());

			ctx.channel().attr(ServerConstants.MESSAGE_ACTOR_KEY).set(params.getStartupContext().buildMessageActor(iSession));
			ctx.channel().attr(ServerConstants.HTTP_WS_HEADER_KEY).set(headers);
			ctx.channel().attr(ServerConstants.HANDLER_PARAM_KEY).set(params);
			ChannelUtil.bindSession(iSession);
		}
		super.userEventTriggered(ctx, evt);
	}

	public WebsocketServerHandler (HttpBootstrapParams params) {
		this.params = params;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MessageContent content) throws Exception {
		ChannelUtil.channelRead(ctx.channel(), params, content);
	}


	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ChannelUtil.cause(params.getStartupContext(), ctx.channel(), cause);
	}
}
