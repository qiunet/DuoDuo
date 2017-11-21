package org.qiunet.flash.handler.netty.server.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.qiunet.flash.handler.netty.param.HttpBootstrapParams;
import org.qiunet.flash.handler.netty.server.http.init.NettyHttpServerInitializer;
import org.qiunet.utils.nonSyncQuene.factory.DefaultThreadFactory;
/**
 * Created by qiunet.
 * 17/11/11
 */
public class NettyHttpServer {

	/***
	 * 启动
	 * @param params  启动使用的端口等
	 */
	public void start(HttpBootstrapParams params) throws Exception {
		// Configure SSL.
		final SslContext sslCtx;
		if (params.isSsl()) {
			SelfSignedCertificate ssc = new SelfSignedCertificate();
			sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
		} else {
			sslCtx = null;
		}


		EventLoopGroup boss = new NioEventLoopGroup(1, new DefaultThreadFactory("http-boss-event-loop-"));
		EventLoopGroup worker = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() , new DefaultThreadFactory("http-worker-event-loop-"));
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(boss, worker);

			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.childHandler(new NettyHttpServerInitializer(sslCtx, params));

			bootstrap.option(ChannelOption.SO_BACKLOG, 128);
			bootstrap.option(ChannelOption.SO_RCVBUF, 10240);
			bootstrap.option(ChannelOption.SO_SNDBUF, 10240);
			bootstrap.option(ChannelOption.TCP_NODELAY, true);
			bootstrap.option(ChannelOption.SO_LINGER, 0);
			bootstrap.option(ChannelOption.SO_REUSEADDR, true);

			ChannelFuture f = bootstrap.bind(params.getAddress()).sync();
			System.err.println("Http server is started by" +
					(params.isSsl()? "HTTPS" : "http") + " mode.");
			f.channel().closeFuture().sync();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
}
