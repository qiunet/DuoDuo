package org.qiunet.flash.handler.netty.tcp.init;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.qiunet.flash.handler.netty.protocol.ProtoBufEncoder;
import org.qiunet.flash.handler.netty.protocol.ProtobufDecoder;
import org.qiunet.flash.handler.netty.tcp.handler.NettyTcpServerHandler;

/**
 * Created by qiunet.
 * 17/8/13
 */
public class NettyTcpServerInitializer extends ChannelInitializer<SocketChannel> {


	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("Encoder", new ProtoBufEncoder());
		pipeline.addLast("Decoder", new ProtobufDecoder());
		pipeline.addLast("handler", new NettyTcpServerHandler());
	}
}
