package org.qiunet.flash.handler.netty.server.http.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.*;
import org.qiunet.flash.handler.common.enums.HandlerType;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.request.websocket.IWebSocketRequestContext;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.mapping.RequestHandlerMapping;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by qiunet.
 * 17/12/1
 */
public class WebsocketServerHandler  extends SimpleChannelInboundHandler<MessageContent> {
	private static final Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);

	private HttpBootstrapParams params;
	private HttpHeaders headers;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.channel().attr(ServerConstants.HANDLER_TYPE_KEY).set(HandlerType.WEB_SOCKET);
	}

	public WebsocketServerHandler (HttpHeaders headers, HttpBootstrapParams params) {
		this.params = params;
		this.headers = headers;
	}
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MessageContent content) throws Exception {
		IHandler handler = RequestHandlerMapping.getInstance().getHandler(content);
		if (handler == null) {
			ctx.writeAndFlush(params.getErrorMessage().getHandlerNotFound().encode()).addListener(ChannelFutureListener.CLOSE);
//			ctx.close(); // 应刘文要求. 觉得没必要关闭通道.
			return;
		}

		IWebSocketRequestContext context = handler.getDataType().createWebSocketRequestContext(content, ctx, handler, params, headers);
		if (ctx.channel().isActive()) {
			handler.getHandlerType().processRequest(context);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("Exception : ", cause);
		ctx.writeAndFlush(params.getErrorMessage().exception(cause).encode()).addListener(ChannelFutureListener.CLOSE);
		ctx.close();
	}
}
