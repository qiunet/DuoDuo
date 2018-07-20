package org.qiunet.flash.handler.netty.server.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.qiunet.flash.handler.netty.server.INettyServer;
import org.qiunet.flash.handler.netty.server.param.UdpBootstrapParam;
import org.qiunet.flash.handler.netty.server.udp.handler.UdpServerHandler;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/7/19 14:56
 **/
public class NettyUdpServer  implements Runnable, INettyServer {
	private Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);

	private UdpBootstrapParam params;

	private ChannelFuture channelFuture;

	public NettyUdpServer(UdpBootstrapParam params) {
		this.params = params;
	}

	@Override
	public void run() {
		NioEventLoopGroup group = new NioEventLoopGroup();

		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group);
		bootstrap.option(ChannelOption.SO_REUSEADDR, true);
		// buffer 能容纳10M的数据.
		bootstrap.option(ChannelOption.SO_RCVBUF, 10 * 1024 * 1024);
		bootstrap.channel(NioDatagramChannel.class);
		bootstrap.handler(new UdpServerHandler());
		try {
			this.channelFuture = bootstrap.bind(params.getAddress());

			logger.error("[NettyUdpServer]  Udp server is Listener on port ["+ ((InetSocketAddress) params.getAddress()).getPort()+"]");
			this.channelFuture.channel().closeFuture().await();
		} catch (InterruptedException e) {
			logger.error("[NettyUdpServer] is Exception! ", e);
		}finally {
			logger.error("[NettyUdpServer] is shutdown! ");
			group.shutdownGracefully();
		}
	}

	@Override
	public void shutdown() {
		this.channelFuture.channel().close();
	}
}
