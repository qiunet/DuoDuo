package org.qiunet.acceptor.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.apache.log4j.Logger;
import org.qiunet.acceptor.log.LogData;
import org.qiunet.acceptor.log.LogDataRegister;
import org.qiunet.acceptor.log.LoggerUtil;
import org.qiunet.logger.sender.MsgHeader;
import org.qiunet.utils.args.ArgsMapping;


public class UDPServer {
	private final Logger logger = LoggerUtil.getOutLogger();

	private int port;

	public UDPServer(ArgsMapping mapping) {
		this.port = mapping.getInt("port", 8888);
		NioEventLoopGroup group = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group);
		bootstrap.option(ChannelOption.SO_REUSEADDR, true);
		// buffer 能容纳10M的数据.
		bootstrap.option(ChannelOption.SO_RCVBUF, 10 * 1024 * 1024);
		bootstrap.channel(NioDatagramChannel.class);
		bootstrap.handler(new UdpServerHandler());
		try {
			bootstrap.bind(port).sync().channel().closeFuture().await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			logger.info("udp server is listener at port["+UDPServer.this.port+"]");
		}

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
			int length = msg.content().readableBytes();

			if (length < MsgHeader.MESSAGE_HEADER_LENGTH){
				logger.error("udp acceptor length ["+length+"] error! ");
				return;
			}
			ctx.flush();

			LogDataRegister.getInstance().addLogNode(new LogData(msg.content().copy()));
		}
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			logger.error("Acceptor is ERROR: ",cause);
			ctx.close();
		}
	}
}
