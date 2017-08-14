package org.qiunet.flash.handler.netty.server.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.qiunet.flash.handler.netty.server.tcp.init.NettyTcpServerInitializer;

import java.net.SocketAddress;

/**
 * Created by qiunet.
 * 17/8/13
 */
public final class NettyTcpServer {
	/***
	 * 启动
	 * @param localAddress  启动使用的端口等
	 */
	public void start(SocketAddress localAddress){
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(boss, worker);
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.childHandler(new NettyTcpServerInitializer());
			bootstrap.option(ChannelOption.SO_BACKLOG, 128);

			ChannelFuture f = bootstrap.bind(localAddress).sync();

			f.channel().closeFuture().sync();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
}
