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
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.sender.IChannelMessageSender;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.context.session.config.DSessionConnectParam;
import org.qiunet.flash.handler.netty.client.param.WebSocketClientParams;
import org.qiunet.flash.handler.netty.client.trigger.IPersistConnResponseTrigger;
import org.qiunet.flash.handler.netty.coder.WebSocketDecoder;
import org.qiunet.flash.handler.netty.coder.WebSocketEncoder;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.http.handler.WebSocketFrameToByteBufHandler;
import org.qiunet.utils.async.factory.DefaultThreadFactory;
import org.qiunet.utils.async.future.DPromise;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.concurrent.ExecutionException;

/**
 * webSocket 客户端
 *
 * Created by qiunet.
 * 17/12/1
 */
public class NettyWebSocketClient implements IChannelMessageSender {
	private static final NioEventLoopGroup group = new NioEventLoopGroup(1 , new DefaultThreadFactory("netty-web-socket-client-event-loop-"));
	private final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	private final IPersistConnResponseTrigger trigger;
	private final WebSocketClientParams params;
	private final Bootstrap bootstrap;
	private ISession session;

	private NettyWebSocketClient(WebSocketClientParams params, IPersistConnResponseTrigger trigger) {
		this.trigger = trigger;
		this.params = params;

		bootstrap = new Bootstrap();
		bootstrap.group(group);

		bootstrap.channel(NioSocketChannel.class);
		bootstrap.option(ChannelOption.TCP_NODELAY,true);
		bootstrap.handler(new NettyWebSocketClient.NettyClientInitializer());
	}

	public static ISession create(WebSocketClientParams params, IPersistConnResponseTrigger trigger){
		NettyWebSocketClient client = new NettyWebSocketClient(params, trigger);
		return client.connect();
	}

	private ISession connect() {
		DPromise<ChannelFuture> promise = DPromise.create();
		ChannelFuture future = bootstrap.connect(params.getAddress());
		future.addListener(f1 -> {
			ChannelFuture handshakeFuture = ((NettyClientHandler) future.channel().pipeline().get("NettyClientHandler")).handshakeFuture();
			promise.trySuccess(handshakeFuture);
		});
		return (this.session = new DSession(DSessionConnectParam.newBuilder(() -> {
			try {
				return promise.get();
			} catch (InterruptedException | ExecutionException e) {
				throw new CustomException(e, "Connect to server {} error!", params.getURI());
			}
		}).build()));
	}

	private class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
			pipeline.addLast("HttpClientCodec", new HttpClientCodec());
			pipeline.addLast("HttpObjectAggregator", new HttpObjectAggregator(1024*1024*2));
			pipeline.addLast("NettyClientHandler", new NettyClientHandler());
		}
	}


	private class NettyClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {
		private final WebSocketClientHandshaker handshaker;
		private ChannelPromise handshakeFuture;
		public ChannelFuture handshakeFuture() {
			return handshakeFuture;
		}

		public NettyClientHandler(){
			this.handshaker = WebSocketClientHandshakerFactory.newHandshaker(
				params.getURI(), WebSocketVersion.V13, null, true, new DefaultHttpHeaders());
		}


		@Override
		public void handlerAdded(ChannelHandlerContext ctx) {
			handshakeFuture = ctx.newPromise();
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) {
			handshaker.handshake(ctx.channel());
		}

		@Override
		public void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
			if (!handshaker.isHandshakeComplete()) {
				try {
					handshaker.finishHandshake(ctx.channel(), msg);

					ChannelPipeline pipeline = ctx.channel().pipeline();
					pipeline.remove(this);


					pipeline.addLast("WebSocketFrameToByteBufHandler", new WebSocketFrameToByteBufHandler());
					pipeline.addLast("WebSocketDecoder", new WebSocketDecoder(params.getMaxReceivedLength(), params.isEncryption()));
					pipeline.addLast("WebSocketServerHandler", new NettyWSClientHandler());
					pipeline.addLast("encode", new WebSocketEncoder());

					ctx.channel().attr(ServerConstants.PROTOCOL_HEADER_ADAPTER).set(params.getProtocolHeaderType());
					handshakeFuture.setSuccess();
				} catch (WebSocketHandshakeException e) {
					handshakeFuture.setFailure(e);
					logger.error("WebSocket Client failed to connect", e);
				}

				ctx.fireChannelRead(msg.retain());
			}
		}
	}

	@Override
	public ISession getSender() {
		return session;
	}

	private class NettyWSClientHandler extends SimpleChannelInboundHandler<MessageContent> {
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, MessageContent msg) throws Exception {
			trigger.response(session, msg);
		}
	}
	public static void shutdown(){
		if (! group.isShutdown()) group.shutdownGracefully();
	}
}
