package org.qiunet.flash.handler.netty.server.http.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
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
import org.qiunet.flash.handler.context.request.data.ChannelDataMapping;
import org.qiunet.flash.handler.context.request.http.IHttpRequestContext;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.mapping.UrlRequestHandlerMapping;
import org.qiunet.flash.handler.netty.coder.WebSocketServerDecoder;
import org.qiunet.flash.handler.netty.coder.WebSocketServerEncoder;
import org.qiunet.flash.handler.netty.server.config.ServerBootStrapConfig;
import org.qiunet.flash.handler.netty.server.idle.NettyIdleCheckHandler;
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

	private final ServerBootStrapConfig config;

	public HttpServerHandler (ServerBootStrapConfig config) {
		this.config = config;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ChannelUtil.sendHttpResponseStatusAndClose(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
		logger.error("HttpServerHandler throw Exception : ", cause);
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
		if (! msg.decoderResult().isSuccess()) {
			ChannelUtil.sendHttpResponseStatusAndClose(ctx, HttpResponseStatus.BAD_REQUEST);
			return;
		}

		// 探测程序返回200
		if (msg.uri().equals("/") || msg.uri().equals("/favicon.ico")) {
			ChannelUtil.sendHttpResponseStatusAndClose(ctx, HttpResponseStatus.OK);
			return;
		}

		URI uri;
		try {
			uri = URI.create(msg.uri());
		}catch (Exception e){
			ChannelUtil.sendHttpResponseStatusAndClose(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
			return;
		}

		try {
			if (config.getHttpBootstrapConfig().getGameURIPath().equals(uri.getRawPath())) {
				// 游戏的请求
				handlerGameUriPathRequest(ctx, msg);
			} else if (config.getHttpBootstrapConfig().getWebsocketPath() != null && config.getHttpBootstrapConfig().getWebsocketPath().equals(uri.getRawPath())) {
				// 升级握手信息
				handlerWebSocketHandShark(ctx, msg);
			}else {
				// 普通的uriPath类型的请求. 可以是游戏外部调用的. 可以随便传入 json什么的.
				handlerOtherUriPathRequest(ctx, msg, uri.getRawPath());
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

		pipeline.addLast("IdleStateHandler", new IdleStateHandler(config.getReadIdleCheckSeconds(), 0, 0));
		pipeline.addLast("NettyIdleCheckHandler", new NettyIdleCheckHandler());
		pipeline.addLast("WebSocketServerProtocolHandler", new WebSocketServerProtocolHandler(WebSocketServerProtocolConfig.newBuilder()
			.maxFramePayloadLength(config.getMaxReceivedLength())
			.websocketPath(config.getHttpBootstrapConfig().getWebsocketPath())
			.handleCloseFrames(true)
			.build()));
		pipeline.addLast("WriteTimeoutHandler", new WriteTimeoutHandler(30));
		pipeline.addLast("WebSocketFrameToByteBufHandler", new WebSocketFrameToByteBufHandler());
		pipeline.addLast("WebSocketDecoder", new WebSocketServerDecoder(config.getMaxReceivedLength(), config.isEncryption()));
		pipeline.addLast("WebSocketServerHandler", new WebsocketServerHandler(config));
		pipeline.addLast("WebSocketEncoder", new WebSocketServerEncoder());

		ctx.channel().config().setOption(ChannelOption.SO_SNDBUF, 1024 * 128);
		ctx.channel().config().setOption(ChannelOption.SO_RCVBUF, 1024 * 128);
		ctx.channel().config().setOption(ChannelOption.TCP_NODELAY, true);
		pipeline.remove(this);
	}

	/***
	 * 处理游戏请求
	 * @return
	 */
	private void handlerGameUriPathRequest(ChannelHandlerContext ctx, FullHttpRequest request){
		IProtocolHeader protocolHeader = ChannelUtil.getProtocolHeader(ctx.channel());
		IProtocolHeader.ProtocolHeader header = protocolHeader.serverNormalIn(request.content(), null);
		if (! header.isValidMessage()) {
			logger.error("Invalid message magic! client is "+ header);
			// encryption 不对, 不被认证的请求
			ChannelUtil.sendHttpResponseStatusAndClose(ctx, HttpResponseStatus.UNAUTHORIZED);
			return;
		}

		MessageContent content = MessageContent.valueOf(header, request.content().readRetainedSlice(header.getLength()));
		try {
			if (config.isEncryption() && ! header.validEncryption(content.byteBuffer())) {
				// encryption 不对, 不被认证的请求
				ChannelUtil.sendHttpResponseStatusAndClose(ctx, HttpResponseStatus.UNAUTHORIZED);
				return;
			}
			this.handlerRequest(() -> ChannelDataMapping.getHandler(content.getProtocolId()), content, ctx, request);
		}finally {
			content.release();
		}
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

		IHttpRequestContext context = handler.getDataType().createHttpRequestContext(content, ctx.channel(), handler, config, request);
		ThreadPoolManager.NORMAL.submit(() -> {
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
		MessageContent content = MessageContent.valueOf(uriPath, request.content().retain());
		try {
			this.handlerRequest(() -> UrlRequestHandlerMapping.getHandler(content.getUriPath()), content, ctx, request);
		}finally {
			content.release();
		}
	}
}
