package org.qiunet.flash.handler.netty.server.tcp.init;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.qiunet.flash.handler.netty.coder.ChannelChoiceDecoder;
import org.qiunet.flash.handler.netty.server.param.ServerBootStrapParam;

/**
 * Created by qiunet.
 * 17/8/13
 */
public class NettyTcpServerInitializer extends ChannelInitializer<SocketChannel> {
	private final ServerBootStrapParam param;
	public NettyTcpServerInitializer(ServerBootStrapParam param) {
		this.param = param;
	}
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(ChannelChoiceDecoder.valueOf(param));
	}
}
