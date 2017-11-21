package org.qiunet.flash.handler.netty.server.tcp.init;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.qiunet.flash.handler.netty.protocol.Encoder;
import org.qiunet.flash.handler.netty.protocol.Decoder;
import org.qiunet.flash.handler.netty.server.tcp.handler.TcpServerHandler;
import org.qiunet.flash.handler.param.TcpBootstrapParams;

/**
 * Created by qiunet.
 * 17/8/13
 */
public class NettyTcpServerInitializer extends ChannelInitializer<SocketChannel> {
	private TcpBootstrapParams params;
	public NettyTcpServerInitializer(TcpBootstrapParams params) {
		this.params = params;
	}
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("Encoder", new Encoder());
		pipeline.addLast("Decoder", new Decoder());
		pipeline.addLast("handler", new TcpServerHandler(params));
	}
}
