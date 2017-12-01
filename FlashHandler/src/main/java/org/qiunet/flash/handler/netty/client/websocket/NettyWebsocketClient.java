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
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import org.qiunet.flash.handler.context.header.MessageContent;

import java.net.URI;

/**
 * Created by qiunet.
 * 17/12/1
 */
public class NettyWebsocketClient {
	private ChannelHandlerContext channelHandlerContext;
	private IWebsocketResponseTrigger trigger;

	public NettyWebsocketClient(URI uri, IWebsocketResponseTrigger trigger) {
		this.trigger = trigger;

		NioEventLoopGroup group = new NioEventLoopGroup(1);
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group);

		NettyClientHandler handler = new NettyClientHandler(
				WebSocketClientHandshakerFactory.newHandshaker(
						uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders()));

		bootstrap.channel(NioSocketChannel.class);
		bootstrap.option(ChannelOption.TCP_NODELAY,true);
		bootstrap.handler(new NettyWebsocketClient.NettyClientInitializer(handler));
		try {
			ChannelFuture future = bootstrap.connect(uri.getHost(), uri.getPort()).sync();
			handler.handshakeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void sendTcpMessage(WebSocketFrame frame){
		channelHandlerContext.channel().writeAndFlush(frame);
	}

	public void close(){
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		channelHandlerContext.channel().close();
	}


	private class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
		private NettyClientHandler handler;
		public NettyClientInitializer(NettyClientHandler handler) {
			this.handler = handler;
		}
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
			pipeline.addLast(new HttpClientCodec(),
					new HttpObjectAggregator(1024*1024*2),
					WebSocketClientCompressionHandler.INSTANCE,
					handler);
		}
	}


	private class NettyClientHandler extends ChannelInboundHandlerAdapter {
		private WebSocketClientHandshaker handshaker;
		private ChannelPromise handshakeFuture;

		public ChannelFuture handshakeFuture() {
			return handshakeFuture;
		}

		public NettyClientHandler(WebSocketClientHandshaker handshaker){
			this.handshaker = handshaker;
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
		public void channelInactive(ChannelHandlerContext ctx) {
			System.out.println("WebSocket Client disconnected!");
		}


		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			if (!handshaker.isHandshakeComplete()) {
				try {
					handshaker.finishHandshake(ctx.channel(), (FullHttpResponse) msg);
					System.out.println("WebSocket Client connected!");
					handshakeFuture.setSuccess();
				} catch (WebSocketHandshakeException e) {
					System.out.println("WebSocket Client failed to connect");
					handshakeFuture.setFailure(e);
				}
				return;
			}

			if (msg instanceof FullHttpResponse) {
				throw new IllegalStateException("Unexpected FullHttpResponse (getStatus=" + ((FullHttpResponse) msg).status());
			}

			if (msg instanceof TextWebSocketFrame) {
				TextWebSocketFrame textFrame = (TextWebSocketFrame) msg;
				System.out.println("WebSocket Client received message: " + textFrame.text());
			} else if (msg instanceof PongWebSocketFrame) {
				System.out.println("WebSocket Client received pong");
			} else if (msg instanceof CloseWebSocketFrame) {
				System.out.println("WebSocket Client received closing");
				ctx.close();
			}
			trigger.response(((WebSocketFrame) msg));
		}
	}
}
