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
import org.qiunet.flash.handler.netty.client.ILongConnClient;
import org.qiunet.flash.handler.netty.client.param.WebSocketClientParams;
import org.qiunet.flash.handler.netty.client.trigger.ILongConnResponseTrigger;
import org.qiunet.flash.handler.netty.coder.WebSocketDecoder;
import org.qiunet.flash.handler.netty.coder.WebSocketEncoder;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.http.handler.WebSocketFrameToByteBufHandler;
import org.qiunet.utils.asyncQuene.factory.DefaultThreadFactory;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

/**
 * Created by qiunet.
 * 17/12/1
 */
public class NettyWebsocketClient implements ILongConnClient {
	private static final NioEventLoopGroup group = new NioEventLoopGroup(1 , new DefaultThreadFactory("netty-web-socket-client-event-loop-"));
	private Logger logger = LoggerType.DUODUO.getLogger();
	private ChannelHandlerContext channelHandlerContext;
	private ILongConnResponseTrigger trigger;

	public NettyWebsocketClient(WebSocketClientParams params, ILongConnResponseTrigger trigger) {
		this.trigger = trigger;

		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group);

		bootstrap.channel(NioSocketChannel.class);
		bootstrap.option(ChannelOption.TCP_NODELAY,true);
		bootstrap.handler(new NettyWebsocketClient.NettyClientInitializer(params));
		try {
			ChannelFuture future = bootstrap.connect(params.getAddress()).sync();
			((NettyClientHandler) future.channel().pipeline().get("NettyClientHandler")).handshakeFuture().sync();
		} catch (Exception e) {
			logger.error("Exception", e);
		}
	}
	@Override
	public void sendMessage(MessageContent content){
		channelHandlerContext.channel().writeAndFlush(content);
	}

	@Override
	public void close(){
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		channelHandlerContext.channel().close();
	}


	private class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
		private WebSocketClientParams params;
		public NettyClientInitializer(WebSocketClientParams params) {
			this.params = params;
		}
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ch.attr(ServerConstants.PROTOCOL_HEADER_ADAPTER).set(params.getProtocolHeaderAdapter());
			ChannelPipeline pipeline = ch.pipeline();
			pipeline.addLast("HttpClientCodec", new HttpClientCodec());
			pipeline.addLast("HttpObjectAggregator", new HttpObjectAggregator(1024*1024*2));
			pipeline.addLast("NettyClientHandler", new NettyClientHandler(params));
		}
	}


	private class NettyClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {
		private WebSocketClientHandshaker handshaker;
		private ChannelPromise handshakeFuture;
		private WebSocketClientParams params;
		public ChannelFuture handshakeFuture() {
			return handshakeFuture;
		}

		public NettyClientHandler(WebSocketClientParams params){
			this.params = params;

			this.handshaker = WebSocketClientHandshakerFactory.newHandshaker(
				params.getURI(), WebSocketVersion.V13, null, true, new DefaultHttpHeaders());
		}


		@Override
		public void handlerAdded(ChannelHandlerContext ctx) {
			handshakeFuture = ctx.newPromise();
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) {
			channelHandlerContext = ctx;
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

					handshakeFuture.setSuccess();
				} catch (WebSocketHandshakeException e) {
					System.out.println("WebSocket Client failed to connect");
					handshakeFuture.setFailure(e);
				}
				ctx.fireChannelRead(msg.retain());
			}
		}
	}

	private class NettyWSClientHandler extends SimpleChannelInboundHandler<MessageContent> {
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, MessageContent msg) throws Exception {
			trigger.response(msg);
		}
	}
	public static void shutdown(){
		if (! group.isShutdown()) group.shutdownGracefully();
	}
}
