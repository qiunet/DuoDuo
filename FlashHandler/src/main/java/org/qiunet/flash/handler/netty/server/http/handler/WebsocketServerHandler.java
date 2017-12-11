package org.qiunet.flash.handler.netty.server.http.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import org.qiunet.flash.handler.acceptor.Acceptor;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.header.ProtocolHeader;
import org.qiunet.flash.handler.context.request.websocket.IWebSocketRequestContext;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.context.session.SessionManager;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.utils.encryptAndDecrypt.CrcUtil;
import org.qiunet.utils.logger.LoggerManager;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.logger.log.QLogger;

import java.util.Arrays;

/**
 * Created by qiunet.
 * 17/12/1
 */
public class WebsocketServerHandler  extends SimpleChannelInboundHandler<WebSocketFrame> {
	private static final SessionManager<String, ISession<String>> sessionManager = SessionManager.getInstance();

	private static final QLogger logger = LoggerManager.getLogger(LoggerType.FLASH_HANDLER);
	private Acceptor acceptor = Acceptor.getInstance();

	private WebSocketServerHandshaker handshaker;
	private HttpBootstrapParams params;

	public WebsocketServerHandler (HttpBootstrapParams params) {
		this.params = params;
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		sessionManager.addSession(params.getSessionBuilder().createSession(ctx));
		params.getSessionEvent().sessionRegistered(ctx);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		params.getSessionEvent().sessionUnregistered(ctx);
		sessionManager.removeSession(ctx.channel().id().asLongText());
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

		this.handshaker.handshake(ctx.channel(), request);
	}

	/***
	 * 反序列化
	 * @param ctx
	 * @param msg
	 * @return
	 */
	private MessageContent decode(ChannelHandlerContext ctx, WebSocketFrame msg) {

		ProtocolHeader header = new ProtocolHeader(msg.content());
		if (! header.isMagicValid()) {
			logger.error("Invalid message, magic is error! "+ Arrays.toString(header.getMagic()));
			ctx.channel().close();
			return null;
		}

		if (header.getLength() <= 0 || header.getLength() > params.getMaxReceivedLength()) {
			logger.error("Invalid message, length is error! length is : "+ header.getLength());
			ctx.channel().close();
			return null;
		}

		byte [] bytes = new byte[header.getLength()];
		msg.content().readBytes(bytes);

		if (params.isCrc() && ! header.crcIsValid(CrcUtil.getCrc32Value(bytes))) {
			logger.error("Invalid message crc! server is : "+ CrcUtil.getCrc32Value(bytes) +" client is "+header.getCrc());
			ctx.channel().close();
			return null;
		}

		MessageContent context = new MessageContent(header.getProtocolId(), bytes);
		return context;
	}
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
		MessageContent content = decode(ctx, msg);
		if (content == null) return;

		IHandler handler = params.getAdapter().getHandler(content);
		if (handler == null) {
			ctx.writeAndFlush(params.getErrorMessage().getHandlerNotFound()).addListener(ChannelFutureListener.CLOSE);
			ctx.close();
			return;
		}

		// 更新最后时间 方便去除很久没有心跳的channel
		sessionManager.getSession(ctx.channel().id().asLongText()).setLastPackageTimeStamp();

		IWebSocketRequestContext context = params.getAdapter().createWebSocketRequestContext(content, ctx, handler, params);
		acceptor.process(context);
	}
}
