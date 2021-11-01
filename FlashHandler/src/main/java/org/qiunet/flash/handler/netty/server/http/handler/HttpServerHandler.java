package org.qiunet.flash.handler.netty.server.http.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolConfig;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.context.header.IProtocolHeaderType;
import org.qiunet.flash.handler.context.request.data.ChannelDataMapping;
import org.qiunet.flash.handler.context.request.http.IHttpRequestContext;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.mapping.UrlRequestHandlerMapping;
import org.qiunet.flash.handler.netty.coder.WebSocketDecoder;
import org.qiunet.flash.handler.netty.coder.WebSocketEncoder;
import org.qiunet.flash.handler.netty.server.idle.NettyIdleCheckHandler;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.net.URI;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * handler 是每次处理new 一个新的. 如果keep alive 则使用同一个实例.
 *
 * Created by qiunet.
 * 17/11/11
 */
public class HttpServerHandler  extends SimpleChannelInboundHandler<FullHttpRequest> {
	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();

	private final HttpBootstrapParams params;

	public HttpServerHandler (HttpBootstrapParams params) {
		this.params = params;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("HttpServerHandler throw Exception : ", cause);
		sendHttpResponseStatusAndClose(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
		ctx.close();
	}
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		FullHttpRequest request = (msg);
		if (! request.decoderResult().isSuccess()) {
			sendHttpResponseStatusAndClose(ctx, HttpResponseStatus.BAD_REQUEST);
			return;
		}
		if (request.uri().equals("/favicon.ico")) {
			sendHttpResponseStatusAndClose(ctx, HttpResponseStatus.NOT_FOUND);
			return;
		}
		try {
			URI uri = URI.create(request.uri());

			if (params.getGameURIPath().equals(uri.getRawPath())) {
				// 游戏的请求
				handlerGameUriPathRequest(ctx, request);
			} else if (params.getWebsocketPath() != null && params.getWebsocketPath().equals(uri.getRawPath())) {
				// 升级握手信息
				handlerWebSocketHandshark(ctx, request);
			}else {
				// 普通的uriPath类型的请求. 可以是游戏外部调用的. 可以随便传入 json什么的.
				handlerOtherUriPathRequest(ctx, request, uri.getRawPath());
			}
		}catch (Exception e) {
			sendHttpResponseStatusAndClose(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);

			logger.error("HttpServerHandler Parse request error: ", e);
		}
	}

	/***
	 * 处理升级握手信息
	 */
	private void handlerWebSocketHandshark(ChannelHandlerContext ctx, FullHttpRequest request){
		ChannelPipeline pipeline = ctx.pipeline();

		pipeline.addLast("IdleStateHandler", new IdleStateHandler(params.getReadIdleCheckSeconds(), 0, 0));
		pipeline.addLast("NettyIdleCheckHandler", new NettyIdleCheckHandler());
		pipeline.addLast("WebSocketServerProtocolHandler", new WebSocketServerProtocolHandler(WebSocketServerProtocolConfig.newBuilder()
			.maxFramePayloadLength(params.getMaxReceivedLength())
			.websocketPath(params.getWebsocketPath())
			.handleCloseFrames(true)
			.build()));
		pipeline.addLast("WriteTimeoutHandler", new WriteTimeoutHandler(30));
		pipeline.addLast("WebSocketFrameToByteBufHandler", new WebSocketFrameToByteBufHandler());
		pipeline.addLast("WebSocketDecoder", new WebSocketDecoder(params.getMaxReceivedLength(), params.isEncryption()));
		pipeline.addLast("WebSocketServerHandler", new WebsocketServerHandler(params));
		pipeline.addLast("WebSocketEncoder", new WebSocketEncoder());

		ctx.fireChannelRead(request.retain());
		pipeline.remove(this);
	}

	/***
	 * 处理游戏请求
	 * @return
	 */
	private void handlerGameUriPathRequest(ChannelHandlerContext ctx, FullHttpRequest request){
		IProtocolHeaderType adapter = ChannelUtil.getProtocolHeaderAdapter(ctx.channel());
		IProtocolHeader header = adapter.inHeader(request.content());
		if (! header.isMagicValid()) {
			logger.error("Invalid message magic! client is "+ header);
			// encryption 不对, 不被认证的请求
			sendHttpResponseStatusAndClose(ctx, HttpResponseStatus.UNAUTHORIZED);
			return;
		}

		ByteBuf byteBuf = request.content().readRetainedSlice(header.getLength());
		MessageContent content = new MessageContent(header.getProtocolId(), byteBuf);
		if (params.isEncryption() && ! header.validEncryption(content.byteBuffer())) {
			// encryption 不对, 不被认证的请求
			sendHttpResponseStatusAndClose(ctx, HttpResponseStatus.UNAUTHORIZED);
			content.release();
			return;
		}
		IHandler handler = ChannelDataMapping.getHandler(content.getProtocolId());
		if (handler == null) {
			sendHttpResponseStatusAndClose(ctx, HttpResponseStatus.NOT_FOUND);
			content.release();
			return;
		}

		IHttpRequestContext context = handler.getDataType().createHttpRequestContext(content, ctx.channel(), handler, params, request);
		try {
			context.handlerRequest();
		} catch (Exception e) {
			sendHttpResponseStatusAndClose(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
			logger.error("Http Exception:", e);
		}
	}
	/***
	 * 处理其它请求
	 * @return
	 */
	private void handlerOtherUriPathRequest(ChannelHandlerContext ctx, FullHttpRequest request, String uriPath){
		ByteBuf byteBuf = request.content();
		MessageContent content = new MessageContent(uriPath, byteBuf.readRetainedSlice(byteBuf.readableBytes()));
		IHandler handler = UrlRequestHandlerMapping.getHandler(content.getUriPath());
		if (handler == null) {
			logger.error("uriPath ["+uriPath+"] not found !");
			sendHttpResponseStatusAndClose(ctx, HttpResponseStatus.NOT_FOUND);
			content.release();
			return;
		}

		IHttpRequestContext context = handler.getDataType().createHttpRequestContext(content, ctx.channel(), handler, params, request);
		try {
			context.handlerRequest();
		} catch (Exception e) {
			sendHttpResponseStatusAndClose(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
			logger.error("Http Exception:", e);
		}
	}

	/***
	 * 发送响应.
	 * @param ctx
	 * @param status 对应的响应码
	 */
	private static void sendHttpResponseStatusAndClose(ChannelHandlerContext ctx, HttpResponseStatus status) {
		logger.error("Http message response status ["+status+"]");
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status);
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
		ctx.close();
	}
}
