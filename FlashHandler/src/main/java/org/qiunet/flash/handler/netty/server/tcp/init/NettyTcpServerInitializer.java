package org.qiunet.flash.handler.netty.server.tcp.init;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.qiunet.flash.handler.netty.coder.TcpSocketDecoder;
import org.qiunet.flash.handler.netty.coder.TcpSocketEncoder;
import org.qiunet.flash.handler.netty.server.idle.NettyIdleCheckHandler;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.flash.handler.netty.server.tcp.handler.TcpServerHandler;

/**
 * Created by qiunet.
 * 17/8/13
 */
public class NettyTcpServerInitializer extends ChannelInitializer<SocketChannel> {
	private final TcpBootstrapParams params;
	public NettyTcpServerInitializer(TcpBootstrapParams params) {
		this.params = params;
	}
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("TcpSocketEncoder", new TcpSocketEncoder());
		pipeline.addLast("TcpSocketDecoder", new TcpSocketDecoder(params.getMaxReceivedLength(), params.isEncryption()));
		pipeline.addLast("handler", new TcpServerHandler(params));
		pipeline.addLast("IdleStateHandler", new IdleStateHandler(params.getReadIdleCheckSeconds(), 0, 0));
		pipeline.addLast(new NettyIdleCheckHandler());
	}
}
