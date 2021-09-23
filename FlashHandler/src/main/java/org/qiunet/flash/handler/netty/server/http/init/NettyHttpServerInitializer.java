package org.qiunet.flash.handler.netty.server.http.init;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.http.handler.HttpServerHandler;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;

/**
 * Created by qiunet.
 * 17/11/11
 */
public class NettyHttpServerInitializer extends ChannelInitializer<SocketChannel> {
	private final HttpBootstrapParams params;
	public NettyHttpServerInitializer(HttpBootstrapParams params) {
		this.params = params;
	}
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		ch.attr(ServerConstants.PROTOCOL_HEADER_ADAPTER).set(params.getProtocolHeaderType());
		p.addLast("HttpServerCodec" ,new HttpServerCodec());
		p.addLast("HttpObjectAggregator", new HttpObjectAggregator(params.getMaxReceivedLength()));
		p.addLast("HttpServerHandler", new HttpServerHandler(params));
	}
}
