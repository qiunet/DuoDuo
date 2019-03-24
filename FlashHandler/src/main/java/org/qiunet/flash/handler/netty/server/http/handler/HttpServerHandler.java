package org.qiunet.flash.handler.netty.server.http.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketFrameDecoder;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.context.header.ProtocolHeader;
import org.qiunet.flash.handler.common.message.UriHttpMessageContent;
import org.qiunet.flash.handler.context.request.http.IHttpRequestContext;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.mapping.RequestHandlerMapping;
import org.qiunet.flash.handler.netty.coder.WebSocketDecoder;
import org.qiunet.flash.handler.netty.coder.WebSocketEncoder;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.utils.encryptAndDecrypt.CrcUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * handler 是每次处理new 一个新的. 如果keep alive 则使用同一个实例.
 *
 * Created by qiunet.
 * 17/11/11
 */
public class HttpServerHandler  extends SimpleChannelInboundHandler<FullHttpRequest> {
	private static final Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);

	private HttpBootstrapParams params;


	public HttpServerHandler (HttpBootstrapParams params) {
		this.params = params;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("HttpServerHandler throw Exception : ", cause);
		sendHttpResonseStatusAndClose(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
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
			sendHttpResonseStatusAndClose(ctx, HttpResponseStatus.BAD_REQUEST);
			return;
		}
		if (request.uri().equals("/favicon.ico")) {
			sendHttpResonseStatusAndClose(ctx, HttpResponseStatus.NOT_FOUND);
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
				ctx.fireChannelRead(msg.retain());
			}else {
				// 普通的uriPath类型的请求. 可以是游戏外部调用的. 可以随便传入 json什么的.
				handlerOtherUriPathRequest(ctx, request, uri.getRawPath());
			}
		}catch (Exception e) {
			sendHttpResonseStatusAndClose(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);

			logger.error("HttpServerHandler Parse request error: ", e);
		}
	}

	/***
	 * 处理升级握手信息
	 */
	private void handlerWebSocketHandshark(ChannelHandlerContext ctx, FullHttpRequest request){
		ChannelPipeline pipeline = ctx.pipeline();
		pipeline.remove(this);

		pipeline.addLast("WebSocketServerProtocolHandler", new WebSocketServerProtocolHandler(params.getWebsocketPath(), null, false, params.getMaxReceivedLength()));
		pipeline.addLast("WebSocketFrameToByteBufHandler", new WebSocketFrameToByteBufHandler());
		pipeline.addLast("WebSocketDecoder", new WebSocketDecoder(params.getMaxReceivedLength(), params.isEncryption()));
		pipeline.addLast("WebSocketServerHandler", new WebsocketServerHandler(request.headers(), params));
		pipeline.addLast("IdleStateHandler", new IdleStateHandler(params.getReadIdleCheckSeconds(), 0, 0));
		pipeline.addLast("WebSocketEncoder", new WebSocketEncoder());
	}

	/***
	 * 处理游戏请求
	 * @return
	 */
	private void handlerGameUriPathRequest(ChannelHandlerContext ctx, FullHttpRequest request){
		IProtocolHeader header = new ProtocolHeader();
		header.parseHeader(request.content());
		if (! header.isMagicValid()) {
			logger.error("Invalid message magic! client is "+ header);
			// encryption 不对, 不被认证的请求
			sendHttpResonseStatusAndClose(ctx, HttpResponseStatus.UNAUTHORIZED);
			return;
		}

		byte [] bytes = new byte[request.content().readableBytes()];
		request.content().readBytes(bytes);
		if (params.isEncryption() && ! header.encryptionValid(CrcUtil.getCrc32Value(bytes))) {
			logger.error("Invalid message encryption! server is : "+ CrcUtil.getCrc32Value(bytes) +" client is "+header);
			// encryption 不对, 不被认证的请求
			sendHttpResonseStatusAndClose(ctx, HttpResponseStatus.UNAUTHORIZED);
			return;
		}
		MessageContent content = new MessageContent(header.getProtocolId(), bytes);
		IHandler handler = RequestHandlerMapping.getInstance().getHandler(content);
		if (handler == null) {
			sendHttpResonseStatusAndClose(ctx, HttpResponseStatus.NOT_FOUND);
			return;
		}

		IHttpRequestContext context = handler.getDataType().createHttpRequestContext(content, ctx, handler, params, request);
		handler.getHandlerType().processRequest(context);
	}
	/***
	 * 处理其它请求
	 * @return
	 */
	private void handlerOtherUriPathRequest(ChannelHandlerContext ctx, FullHttpRequest request, String uriPath){
		byte [] bytes = new byte[request.content().readableBytes()];
		request.content().readBytes(bytes);
		MessageContent content = new UriHttpMessageContent(uriPath, bytes);
		IHandler handler = RequestHandlerMapping.getInstance().getHandler(content);
		if (handler == null) {
			logger.error("uriPath ["+uriPath+"] not found !");
			sendHttpResonseStatusAndClose(ctx, HttpResponseStatus.NOT_FOUND);
			return;
		}

		IHttpRequestContext context = handler.getDataType().createHttpRequestContext(content, ctx, handler, params, request);
		handler.getHandlerType().processRequest(context);
	}

	/***
	 * 发送响应.
	 * @param ctx
	 * @param status 对应的响应码
	 */
	private static void sendHttpResonseStatusAndClose(ChannelHandlerContext ctx, HttpResponseStatus status) {
		logger.error("Http message response status ["+status+"]");
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status);
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
		ctx.close();
	}
}
