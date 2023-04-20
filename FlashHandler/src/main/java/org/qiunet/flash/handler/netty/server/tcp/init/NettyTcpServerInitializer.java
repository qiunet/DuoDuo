package org.qiunet.flash.handler.netty.server.tcp.init;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.qiunet.flash.handler.netty.coder.ChannelChoiceDecoder;
import org.qiunet.flash.handler.netty.server.config.ServerBootStrapConfig;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;

/**
 * Created by qiunet.
 * 17/8/13
 */
public class NettyTcpServerInitializer extends ChannelInitializer<SocketChannel> {
	private final ServerBootStrapConfig config;
	public NettyTcpServerInitializer(ServerBootStrapConfig config) {
		this.config = config;
	}
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.attr(ServerConstants.BOOTSTRAP_CONFIG_KEY).set(config);
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new ChannelChoiceDecoder());
	}
}
