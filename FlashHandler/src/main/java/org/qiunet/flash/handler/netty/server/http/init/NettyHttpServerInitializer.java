package org.qiunet.flash.handler.netty.server.http.init;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.timeout.IdleStateHandler;
import org.qiunet.flash.handler.netty.server.http.handler.HttpServerHandler;
import org.qiunet.flash.handler.netty.server.idle.NettyIdleCheckHandler;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;

/**
 * Created by qiunet.
 * 17/11/11
 */
public class NettyHttpServerInitializer extends ChannelInitializer<SocketChannel> {
	private final SslContext sslCtx;
	private HttpBootstrapParams params;
	public NettyHttpServerInitializer(SslContext sslCtx, HttpBootstrapParams params) {
		this.sslCtx = sslCtx;
		this.params = params;
	}
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		if (sslCtx != null) {
			p.addLast(sslCtx.newHandler(ch.alloc()));
		}
		p.addLast("HttpServerCodec" ,new HttpServerCodec());
		p.addLast("HttpObjectAggregator", new HttpObjectAggregator(params.getMaxReceivedLength()));
		p.addLast("HttpServerHandler", new HttpServerHandler(params));
		p.addLast("IdleStateHandler", new IdleStateHandler(params.getReadIdleCheckSeconds(), 0, 0));
		p.addLast(new NettyIdleCheckHandler());
	}
}
