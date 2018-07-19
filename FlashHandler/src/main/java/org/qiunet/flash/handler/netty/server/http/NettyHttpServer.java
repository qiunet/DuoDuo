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
import org.qiunet.flash.handler.netty.server.INettyServer;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.flash.handler.netty.server.http.init.NettyHttpServerInitializer;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.asyncQuene.factory.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Created by qiunet.
 * 17/11/11
 */
public class NettyHttpServer implements Runnable, INettyServer {
	private Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);
	private ChannelFuture channelFuture;
	private HttpBootstrapParams params;
	/***
	 * 启动
	 * @param params  启动使用的端口等
	 */
	public NettyHttpServer(HttpBootstrapParams params) {
		this.params = params;
	}

	@Override
	public void run() {
		EventLoopGroup boss = new NioEventLoopGroup(1, new DefaultThreadFactory("http-boss-event-loop-"));
		EventLoopGroup worker = new NioEventLoopGroup(0, new DefaultThreadFactory("http-worker-event-loop-"));
		try {
			// Configure SSL.
			final SslContext sslCtx;
			if (params.isSsl()) {
				SelfSignedCertificate ssc = new SelfSignedCertificate();
				sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
			} else {
				sslCtx = null;
			}
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(boss, worker);

			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.childHandler(new NettyHttpServerInitializer(sslCtx, params));
			bootstrap.option(ChannelOption.SO_REUSEADDR, true);
			bootstrap.option(ChannelOption.SO_BACKLOG, 256);
			this.channelFuture = bootstrap.bind(params.getAddress()).sync();
			logger.error("[NettyHttpServer]  Http server is started by " +
					(params.isSsl()? "HTTPS" : "http") + " mode on port ["+ ((InetSocketAddress) params.getAddress()).getPort()+"]");
			this.channelFuture.channel().closeFuture().sync();
		}catch (Exception e) {
			logger.error("[NettyHttpServer] Exception: ", e);
		}finally {
			logger.error("[NettyHttpServer] is shutdown! ");
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}

	/***
	 * 停止
	 */
	@Override
	public void shutdown(){
		this.channelFuture.channel().close();
	}
}
