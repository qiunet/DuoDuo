package org.qiunet.flash.handler.netty.client.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.netty.client.ILongConnClient;
import org.qiunet.flash.handler.netty.client.param.TcpClientParams;
import org.qiunet.flash.handler.netty.client.trigger.ILongConnResponseTrigger;
import org.qiunet.flash.handler.netty.coder.TcpSocketDecoder;
import org.qiunet.flash.handler.netty.coder.TcpSocketEncoder;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.asyncQuene.factory.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Created by qiunet.
 * 17/11/25
 */
public class NettyTcpClient implements ILongConnClient {
	private static final NioEventLoopGroup group = new NioEventLoopGroup(1, new DefaultThreadFactory("netty-tcp-client-event-loop-"));
	private Logger logger = LoggerType.DUODUO.getLogger();
	private ILongConnResponseTrigger trigger;
	private Channel channel;
	public NettyTcpClient(TcpClientParams params, ILongConnResponseTrigger trigger) {
		this.trigger = trigger;
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group);

		bootstrap.channel(NioSocketChannel.class);
		bootstrap.option(ChannelOption.TCP_NODELAY,true);
		bootstrap.handler(new NettyClientInitializer(params));
		try {
			this.channel = bootstrap.connect(params.getAddress()).sync().channel();
		} catch (Exception e) {
			logger.error("Exception", e);
		}
	}
	@Override
	public void sendMessage(MessageContent content){
		channel.writeAndFlush(content);
	}
	@Override
	public void close(){
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void shutdown(){
		if (! group.isShutdown()) group.shutdownGracefully();
	}

	private class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
		private TcpClientParams params;

		public NettyClientInitializer(TcpClientParams params) {
			this.params = params;
		}

		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
			ch.attr(ServerConstants.PROTOCOL_HEADER_ADAPTER).set(params.getProtocolHeaderAdapter());
			pipeline.addLast("TcpSocketEncoder", new TcpSocketEncoder());
			pipeline.addLast("TcpSocketDecoder", new TcpSocketDecoder(1024*1024*2, true));
			pipeline.addLast(new NettyClientHandler());
		}
	}

	private class NettyClientHandler extends SimpleChannelInboundHandler<MessageContent> {
		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
		}

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, MessageContent msg) throws Exception {
			trigger.response(msg);
		}
	}
}
