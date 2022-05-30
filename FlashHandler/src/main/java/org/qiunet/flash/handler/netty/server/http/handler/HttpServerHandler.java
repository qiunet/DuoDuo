package org.qiunet.flash.handler.netty.server.http.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
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
import org.qiunet.utils.thread.ThreadContextData;
import org.qiunet.utils.thread.ThreadPoolManager;
import org.slf4j.Logger;

import java.net.URI;
import java.util.function.Supplier;

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
		ChannelUtil.sendHttpResponseStatusAndClose(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
		ctx.close();
	}


	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
		ctx.fireChannelReadComplete();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		try {
			this.channelRead1(ctx, msg);
		}finally {
			ctx.fireChannelRead(msg.retain());
		}
	}

	protected void channelRead1(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		FullHttpRequest request = (msg);
		if (! request.decoderResult().isSuccess()) {
			ChannelUtil.sendHttpResponseStatusAndClose(ctx, HttpResponseStatus.BAD_REQUEST);
			return;
		}
		if (request.uri().equals("/favicon.ico")) {
			ChannelUtil.sendHttpResponseStatusAndClose(ctx, HttpResponseStatus.NOT_FOUND);
			return;
		}
		URI uri;
		try {
			uri = URI.create(request.uri());
		}catch (Exception e){
			ChannelUtil.sendHttpResponseStatusAndClose(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
			return;
		}

		try {
			if (params.getGameURIPath().equals(uri.getRawPath())) {
				// 游戏的请求
				handlerGameUriPathRequest(ctx, request);
			} else if (params.getWebsocketPath() != null && params.getWebsocketPath().equals(uri.getRawPath())) {
				// 升级握手信息
				handlerWebSocketHandShark(ctx, request);
			}else {
				// 普通的uriPath类型的请求. 可以是游戏外部调用的. 可以随便传入 json什么的.
				handlerOtherUriPathRequest(ctx, request, uri.getRawPath());
			}
		}catch (Exception e) {
			ChannelUtil.sendHttpResponseStatusAndClose(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
			logger.error("HttpServerHandler Parse request error: ", e);
		}
	}

	/***
	 * 处理升级握手信息
	 */
	private void handlerWebSocketHandShark(ChannelHandlerContext ctx, FullHttpRequest request){
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
			ChannelUtil.sendHttpResponseStatusAndClose(ctx, HttpResponseStatus.UNAUTHORIZED);
			return;
		}

		ByteBuf byteBuf = request.content().readRetainedSlice(header.getLength());
		MessageContent content = new MessageContent(header.getProtocolId(), byteBuf);
		if (params.isEncryption() && ! header.validEncryption(content.byteBuffer())) {
			// encryption 不对, 不被认证的请求
			ChannelUtil.sendHttpResponseStatusAndClose(ctx, HttpResponseStatus.UNAUTHORIZED);
			return;
		}
		this.handlerRequest(() -> ChannelDataMapping.getHandler(content.getProtocolId()), content, ctx, request);
	}

	/**
	 * 处理请求
	 * @param handlerGetter
	 * @param content
	 * @param ctx
	 * @param request
	 */
	private void handlerRequest(Supplier<IHandler> handlerGetter, MessageContent content, ChannelHandlerContext ctx, FullHttpRequest request) {
		IHandler handler = handlerGetter.get();
		if (handler == null) {
			logger.error("Handler [{}] not found!", content.toString());
			ChannelUtil.sendHttpResponseStatusAndClose(ctx, HttpResponseStatus.NOT_FOUND);
			return;
		}

		IHttpRequestContext context = handler.getDataType().createHttpRequestContext(content, ctx.channel(), handler, params, request);
		ThreadPoolManager.MESSAGE_HANDLER.submit(() -> {
			try {
				context.handlerRequest();
			} catch (Exception e) {
				ChannelUtil.sendHttpResponseStatusAndClose(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
				logger.error("Http Exception:", e);
			}finally {
				ThreadContextData.removeAll();
			}
		});
	}
	/***
	 * 处理其它请求
	 * @return
	 */
	private void handlerOtherUriPathRequest(ChannelHandlerContext ctx, FullHttpRequest request, String uriPath){
		ByteBuf byteBuf = request.content();
		MessageContent content = new MessageContent(uriPath, byteBuf.readRetainedSlice(byteBuf.readableBytes()));
		this.handlerRequest(() -> UrlRequestHandlerMapping.getHandler(content.getUriPath()), content, ctx, request);
	}
}
