/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.qiunet.flash.handler.netty.client.http;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

/**
 * 给客户端测试使用的一个HttpClient类
 * A simple HTTP client that prints out the content of the HTTP response to
 */
public final class NettyHttpClient {
	private int port;
	private String host;
	private boolean https;
	private boolean keepAlive;
	private HttpClientHandler clientHandler;

	public NettyHttpClient (String host, int port) {
		this(host, port, false, false);
	}

	public NettyHttpClient (String host, int port, boolean https, boolean keepAlive) {
		this.host = host;
		this.port = port;
		this.https = https;
		this.keepAlive = keepAlive;
		clientHandler = new HttpClientHandler();
	}
	/***
	 *
	 * @param queryUri 格式: /back?key1=value1&key2=value2
	 * @param byteBuf post 的数据
	 * @return
	 */
	public FullHttpResponse sendRequest(ByteBuf byteBuf, String queryUri) {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = createBootstrap(group);
			ChannelFuture future = b.connect(host, port).sync();
			clientHandler.sendRequest(byteBuf, queryUri);
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
		return clientHandler.response;
	}

	/***
	 * 得到bootstrap
	 * @return
	 * @throws Exception
	 */
	private Bootstrap createBootstrap(EventLoopGroup group) throws Exception {
		final SslContext sslCtx;
		if (https) {
			sslCtx = SslContextBuilder.forClient()
					.trustManager(InsecureTrustManagerFactory.INSTANCE).build();
		} else {
			sslCtx = null;
		}

		Bootstrap b = new Bootstrap();
		b.group(group)
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>(){
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline p = ch.pipeline();

						if (sslCtx != null) {
							p.addLast(sslCtx.newHandler(ch.alloc()));
						}
						p.addLast(new HttpResponseDecoder());
						p.addLast(new HttpRequestEncoder());
						p.addLast(new HttpObjectAggregator(1024* 1024 * 2));
						p.addLast(clientHandler);
					}
				});
		b.option(ChannelOption.TCP_NODELAY, true);
		return b;
	}


	public class HttpClientHandler extends SimpleChannelInboundHandler<HttpObject> {
		private FullHttpResponse response;
		private ChannelHandlerContext ctx;

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			this.ctx = ctx;
		}

		/****
		 * 如果是keepalive 可以重用channel
		 * 这里因为是客户端测试, 特地使用阻塞来同步. 服务端一般不能这样
		 * @param queryUri 格式: /back?key1=value1&key2=value2
		 * @param byteBuf post 的数据
		 * @return
		 */
		public FullHttpResponse sendRequest(ByteBuf byteBuf, String queryUri) {
			this.response = null;
			DefaultFullHttpRequest request = new DefaultFullHttpRequest(
					HttpVersion.HTTP_1_1, HttpMethod.POST, queryUri, byteBuf);

			request.headers().set(HttpHeaderNames.HOST, host);
			if (keepAlive ) {
				request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
			}
			request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);
			request.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
			ctx.channel().writeAndFlush(request);
			return this.response;
		}

		@Override
		public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
			if (!(msg instanceof FullHttpResponse)) {
				return;
			}

			this.response =  ((FullHttpResponse) msg).copy();
			if (! keepAlive || ! HttpHeaderValues.KEEP_ALIVE.toString().equals(response.headers().get(HttpHeaderNames.CONNECTION))) {
				ctx.close();
			}
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
			cause.printStackTrace();
			ctx.close();
		}
	}
}
