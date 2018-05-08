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
import org.qiunet.flash.handler.netty.client.trigger.IHttpResponseTrigger;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.asyncQuene.factory.DefaultThreadFactory;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * 给客户端测试使用的一个HttpClient类
 * A simple HTTP client that prints out the content of the HTTP response to
 */
public final class NettyHttpClient {
	private static Logger logger = LoggerFactory.getLogger(LoggerType.FLASH_HANDLER);
	private static NioEventLoopGroup group = new NioEventLoopGroup(1, new DefaultThreadFactory("netty-http-client-event-loop-"));

	/**
	 * 需要关闭NioEventLoop
	 */
	public static void shutdown(){
		group.shutdownGracefully();
	}

	/***
	 * 不阻塞的方式请求.
	 * @param byteBuf
	 * @param url
	 * @param trigger
	 */
	public static void sendRequest(ByteBuf byteBuf, String url, IHttpResponseTrigger trigger) {
		URI uri = URI.create(url);

		HttpClientHandler clientHandler = new HttpClientHandler(trigger);
		try {
			Bootstrap b = createBootstrap(group, clientHandler, uri);
			ChannelFuture future = b.connect(uri.getHost(), uri.getPort()).sync();
			future.channel().writeAndFlush(buildRequest(byteBuf, uri));
		} catch (Exception e) {
			logger.error("Exception", e);
		}
	}
	/***
	 * 阻塞的方式请求
	 * @param byteBuf
	 * @param url
	 */
	public static FullHttpResponse sendRequest(ByteBuf byteBuf, String url) {
		URI uri = URI.create(url);

		HttpClientHandler clientHandler = new HttpClientHandler(null);
		try {
			Bootstrap b = createBootstrap(group, clientHandler, uri);
			ChannelFuture future = b.connect(uri.getHost(), uri.getPort()).sync();
			future.channel().writeAndFlush(buildRequest(byteBuf, uri));
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clientHandler.response;
	}
	/****
	 * 如果是keepalive 可以重用channel
	 * 这里因为是客户端测试, 特地使用阻塞来同步. 服务端一般不能这样
	 * @param uri
	 * @param byteBuf post 的数据
	 * @return
	 */
	private static DefaultFullHttpRequest buildRequest(ByteBuf byteBuf, URI uri) {
		String pathAndQuery = (StringUtil.isEmpty(uri.getRawQuery()) ? uri.getRawPath() : (uri.getRawPath() +'?' + uri.getRawQuery()));

		DefaultFullHttpRequest request = new DefaultFullHttpRequest(
				HttpVersion.HTTP_1_1, HttpMethod.POST, pathAndQuery, byteBuf);

		request.headers().set(HttpHeaderNames.HOST, uri.getHost());
		request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
		request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);
		request.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
		return request;
	}

	/***
	 * 得到bootstrap
	 * @return
	 * @throws Exception
	 */
	private static Bootstrap createBootstrap(NioEventLoopGroup group, final HttpClientHandler clientHandler, URI uri) throws Exception {
		final SslContext sslCtx;
		if ("https".equalsIgnoreCase(uri.getScheme())) {
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


	private static class HttpClientHandler extends SimpleChannelInboundHandler<HttpObject> {
		private IHttpResponseTrigger trigger;
		private FullHttpResponse response;
		HttpClientHandler(IHttpResponseTrigger trigger) {
			this.trigger = trigger;
		}
		@Override
		public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
			if (!(msg instanceof FullHttpResponse)) {
				ctx.close();
				return;
			}
			this.response = ((FullHttpResponse) msg).copy();
			if (trigger != null) this.trigger.response(response);
			ctx.close();
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
			cause.printStackTrace();
			ctx.close();
		}
	}
}
