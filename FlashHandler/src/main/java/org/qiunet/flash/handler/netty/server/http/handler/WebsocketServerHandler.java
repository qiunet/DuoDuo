package org.qiunet.flash.handler.netty.server.http.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.*;
import org.qiunet.flash.handler.common.enums.HandlerType;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.context.header.ProtocolHeader;
import org.qiunet.flash.handler.context.request.websocket.IWebSocketRequestContext;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.mapping.RequestHandlerMapping;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.utils.encryptAndDecrypt.CrcUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by qiunet.
 * 17/12/1
 */
public class WebsocketServerHandler  extends SimpleChannelInboundHandler<WebSocketFrame> {
	private static final Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);

	private WebSocketServerHandshaker handshaker;
	private HttpBootstrapParams params;
	private HttpHeaders headers;

	public WebsocketServerHandler (HttpBootstrapParams params) {
		this.params = params;
	}

	/***
	 * 处理升级握手信息
	 */
	public void handlerWebSocketHandshark(ChannelHandlerContext ctx, FullHttpRequest request){
		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
				"ws://"+request.headers().get(HttpHeaderNames.HOST)+params.getWebsocketPath(),
				null,
				true
		);

		this.handshaker = wsFactory.newHandshaker(request);
		if (this.handshaker == null) {
			WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
			ctx.flush().channel().closeFuture().addListener(ChannelFutureListener.CLOSE);
			return;
		}

		this.headers = request.headers();

		HttpHeaders headers = new DefaultHttpHeaders();
		headers.set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "*");
		this.handshaker.handshake(ctx.channel(), request, headers, ctx.channel().newPromise());

		ChannelPipeline pipeline = ctx.channel().pipeline();
		pipeline.remove("HttpServerHandler");
		pipeline.addLast("WebsocketServerHandler", this);
	}

	/***
	 * 反序列化
	 * @param ctx
	 * @param msg
	 * @return
	 */
	private MessageContent decode(ChannelHandlerContext ctx, WebSocketFrame msg) {
		IProtocolHeader header = new ProtocolHeader().parseHeader(msg.content());
		if (! header.isMagicValid()) {
			logger.error("Invalid message, magic is error! "+ header);
			ctx.channel().close();
			return null;
		}

		if (header.getLength() < 0 || header.getLength() > params.getMaxReceivedLength()) {
			logger.error("Invalid message, length is error! length is : "+ header.getLength());
			ctx.channel().close();
			return null;
		}

		byte [] bytes = new byte[header.getLength()];
		msg.content().readBytes(bytes);

		if (params.isEncryption() && ! header.encryptionValid(CrcUtil.getCrc32Value(bytes))) {
			logger.error("Invalid message encryption! server is : "+ CrcUtil.getCrc32Value(bytes) +" client is "+header);
			ctx.channel().close();
			return null;
		}

		MessageContent context = new MessageContent(header.getProtocolId(), bytes);
		return context;
	}
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
		if (msg instanceof PingWebSocketFrame) {
			msg.content().retain();
			ctx.channel().writeAndFlush(new PongWebSocketFrame(msg.content()));
			return;
		}
		if (msg instanceof PongWebSocketFrame) {
			return;
		}

		if (msg instanceof CloseWebSocketFrame) {
			if (handshaker != null) {
				msg.retain();
				handshaker.close(ctx.channel(), (CloseWebSocketFrame) msg);
			} else {
				ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
			}
			return;
		}


		MessageContent content = decode(ctx, msg);
		if (content == null) return;

		IHandler handler = RequestHandlerMapping.getInstance().getHandler(content);
		if (handler == null) {
			ctx.writeAndFlush(new BinaryWebSocketFrame(params.getErrorMessage().getHandlerNotFound().encode().encodeToByteBuf())).addListener(ChannelFutureListener.CLOSE);
//			ctx.close(); // 应刘文要求. 觉得没必要关闭通道.
			return;
		}
		// 更新最后时间 方便去除很久没有心跳的channel

		IWebSocketRequestContext context = handler.getDataType().createWebSocketRequestContext(content, ctx, handler, params, headers);
		if (ctx.channel().isActive()) {
			handler.getHandlerType().processRequest(context);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("Exception : ", cause);
		ctx.writeAndFlush(new BinaryWebSocketFrame(params.getErrorMessage().exception(cause).encode().encodeToByteBuf())).addListener(ChannelFutureListener.CLOSE);
		ctx.close();
	}
}
