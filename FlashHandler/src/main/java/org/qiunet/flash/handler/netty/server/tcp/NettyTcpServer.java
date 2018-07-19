package org.qiunet.flash.handler.netty.server.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.qiunet.flash.handler.netty.server.INettyServer;
import org.qiunet.flash.handler.netty.server.tcp.init.NettyTcpServerInitializer;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.asyncQuene.factory.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Created by qiunet.
 * 17/8/13
 */
public final class NettyTcpServer implements Runnable, INettyServer {
	private Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);

	private TcpBootstrapParams params;

	private ChannelFuture channelFuture;
	/***
	 * 启动
	 * @param params  启动使用的端口等启动参数
	 */
	public NettyTcpServer(TcpBootstrapParams params) {
		this.params = params;
	}

	@Override
	public void run() {
		EventLoopGroup boss = new NioEventLoopGroup(1, new DefaultThreadFactory("tcp-boss-event-loop-"));
		EventLoopGroup worker = new NioEventLoopGroup(0 , new DefaultThreadFactory("tcp-worker-event-loop-"));
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(boss, worker);

			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.childHandler(new NettyTcpServerInitializer(params));

			bootstrap.option(ChannelOption.SO_BACKLOG, 256);
			bootstrap.option(ChannelOption.SO_REUSEADDR, true);
			bootstrap.option(ChannelOption.SO_RCVBUF, 1024*1024*2);

			this.channelFuture = bootstrap.bind(params.getAddress());
			logger.error("[NettyTcpServer]  Tcp server is Listener on port ["+ ((InetSocketAddress) params.getAddress()).getPort()+"]");
			channelFuture.channel().closeFuture().sync();
		}catch (Exception e) {
			logger.error("[NettyTcpServer] Exception: ", e);
		}finally {
			logger.error("[NettyTcpServer] is shutdown! ");
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
	@Override
	public void shutdown(){
		this.channelFuture.channel().close();
	}
}
