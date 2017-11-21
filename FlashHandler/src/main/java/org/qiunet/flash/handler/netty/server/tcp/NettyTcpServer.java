package org.qiunet.flash.handler.netty.server.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.qiunet.flash.handler.netty.server.tcp.init.NettyTcpServerInitializer;
import org.qiunet.flash.handler.netty.param.TcpBootstrapParams;
import org.qiunet.utils.nonSyncQuene.factory.DefaultThreadFactory;

/**
 * Created by qiunet.
 * 17/8/13
 */
public final class NettyTcpServer {

	/***
	 * 启动
	 * @param params  启动使用的端口等启动参数
	 */
	public void start(TcpBootstrapParams params){
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
			bootstrap.option(ChannelOption.SO_LINGER, 0);
			bootstrap.option(ChannelOption.SO_REUSEADDR, true);

			ChannelFuture f = bootstrap.bind(params.getAddress()).sync();

			f.channel().closeFuture().sync();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
}
