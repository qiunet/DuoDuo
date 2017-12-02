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
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.context.session.SessionManager;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.utils.logger.LoggerManager;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.logger.log.QLogger;

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
		logger.info("handlerAdded "+ctx.channel().id());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		logger.info("handlerRemoved "+ctx.channel().id());
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

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
		ctx.writeAndFlush(msg.retain());
	}
}
