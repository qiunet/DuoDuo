package org.qiunet.flash.handler.netty.server.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.qiunet.flash.handler.netty.server.tcp.init.NettyTcpServerInitializer;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.utils.logger.LoggerManager;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.logger.log.QLogger;
import org.qiunet.utils.nonSyncQuene.factory.DefaultThreadFactory;

import java.net.InetSocketAddress;

/**
 * Created by qiunet.
 * 17/8/13
 */
public final class NettyTcpServer implements Runnable {
	private QLogger qLogger = LoggerManager.getLogger(LoggerType.FLASH_HANDLER);

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
		EventLoopGroup worker = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() , new DefaultThreadFactory("tcp-worker-event-loop-"));
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(boss, worker);

			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.childHandler(new NettyTcpServerInitializer(params));

			bootstrap.option(ChannelOption.SO_BACKLOG, 128);
			bootstrap.option(ChannelOption.SO_RCVBUF, 10240);
			bootstrap.option(ChannelOption.SO_SNDBUF, 10240);
			bootstrap.option(ChannelOption.TCP_NODELAY, true);
			bootstrap.option(ChannelOption.SO_TIMEOUT, 2000);
			bootstrap.option(ChannelOption.SO_REUSEADDR, true);

			this.channelFuture = bootstrap.bind(params.getAddress()).sync();
			qLogger.error("Netty Tcp server is Listener on port ["+ ((InetSocketAddress) params.getAddress()).getPort()+"]");
			channelFuture.channel().closeFuture().sync();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}

	public void shutdown(){
		this.channelFuture.channel().close();
	}
}
