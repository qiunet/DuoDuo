package org.qiunet.flash.handler.netty.client.websocket;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.session.ClientSession;
import org.qiunet.flash.handler.netty.client.param.WebSocketClientConfig;
import org.qiunet.flash.handler.netty.client.trigger.IPersistConnResponseTrigger;
import org.qiunet.flash.handler.netty.coder.WebSocketClientDecoder;
import org.qiunet.flash.handler.netty.coder.WebSocketClientEncoder;
import org.qiunet.flash.handler.netty.server.bound.FlushBalanceHandler;
import org.qiunet.flash.handler.netty.server.bound.NettyCauseHandler;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.http.handler.WebSocketFrameToByteBufHandler;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.utils.async.factory.DefaultThreadFactory;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

/**
 * webSocket 客户端
 *
 * Created by qiunet.
 * 17/12/1
 */
public class NettyWebSocketClient {
	private static final NioEventLoopGroup group = new NioEventLoopGroup( new DefaultThreadFactory("netty-web-socket-client-event-loop-"));
	private final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	private final IPersistConnResponseTrigger trigger;
	private final NettyClientHandler clientHandler;
	private final WebSocketClientConfig config;
	private final Bootstrap bootstrap;

	private NettyWebSocketClient(WebSocketClientConfig config, IPersistConnResponseTrigger trigger) {
		this.trigger = trigger;
		this.config = config;

 		this.clientHandler = new NettyClientHandler();
		this.bootstrap = new Bootstrap();
		this.bootstrap.group(group);

		bootstrap.channel(NioSocketChannel.class);
		bootstrap.option(ChannelOption.TCP_NODELAY,true);
		bootstrap.handler(new NettyWebSocketClient.NettyClientInitializer());
	}

	public static ClientSession create(WebSocketClientConfig config, IPersistConnResponseTrigger trigger){
		NettyWebSocketClient client = new NettyWebSocketClient(config, trigger);
		return client.connect();
	}

	private ClientSession connect() {
		try {
			bootstrap.connect(config.getAddress()).sync();
			return (ClientSession) ChannelUtil.getSession(clientHandler.handshakeFuture.sync().channel());
		} catch (Exception e) {
			LoggerType.DUODUO.error("", e);
		}
		return null;
	}

	private class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
			pipeline.addLast("HttpClientCodec", new HttpClientCodec());
			pipeline.addLast("HttpObjectAggregator", new HttpObjectAggregator(1024 * 128));
			pipeline.addLast("NettyClientHandler", clientHandler);
		}
	}


	private class NettyClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {
		private final WebSocketClientHandshaker handshaker;
		private ChannelPromise handshakeFuture;

		public NettyClientHandler(){
			this.handshaker = WebSocketClientHandshakerFactory.newHandshaker(
				config.getURI(), WebSocketVersion.V13, null, true, new DefaultHttpHeaders());
		}

		@Override
		public void handlerAdded(ChannelHandlerContext ctx) {
			ClientSession clientSession = new ClientSession(ctx.channel(), config.getProtocolHeader());
			ChannelUtil.bindSession(clientSession, ctx.channel());

			clientSession.attachObj(ServerConstants.PROTOCOL_HEADER, config.getProtocolHeader());
			clientSession.attachObj(ServerConstants.HANDLER_TYPE_KEY, ServerConnType.WS);

			handshakeFuture = ctx.newPromise();
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			handshaker.handshake(ctx.channel());
			super.channelActive(ctx);
		}

		@Override
		public void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
			if (!handshaker.isHandshakeComplete()) {
				try {
					handshaker.finishHandshake(ctx.channel(), msg);

					ChannelPipeline pipeline = ctx.channel().pipeline();
					pipeline.remove(this);

					pipeline.addLast("WebSocketFrameToByteBufHandler", new WebSocketFrameToByteBufHandler());
					pipeline.addLast("WebSocketDecoder", new WebSocketClientDecoder(config.getMaxReceivedLength(), config.isEncryption()));
					pipeline.addLast("WebSocketServerHandler", new NettyWSClientHandler());
					pipeline.addLast("encode", new WebSocketClientEncoder());
					pipeline.addLast("FlushBalanceHandler", new FlushBalanceHandler());
					pipeline.addLast("NettyCauseHandler", new NettyCauseHandler());

					handshakeFuture.setSuccess();
				} catch (WebSocketHandshakeException e) {
					handshakeFuture.setFailure(e);
					logger.error("WebSocket Client failed to connect", e);
				}

				ctx.fireChannelRead(msg.retain());
			}
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			LoggerType.DUODUO_FLASH_HANDLER.error("Netty ws client exception: ", cause);
		}
	}

	private class NettyWSClientHandler extends SimpleChannelInboundHandler<MessageContent> {
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, MessageContent msg) throws Exception {
			trigger.response(ChannelUtil.getSession(ctx.channel()), ctx.channel(), msg);
		}
	}
	public static void shutdown(){
		if (! group.isShutdown()) group.shutdownGracefully();
	}
}
