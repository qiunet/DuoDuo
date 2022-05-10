package org.qiunet.flash.handler.netty.server.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.qiunet.flash.handler.netty.server.INettyServer;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.http.init.NettyHttpServerInitializer;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.utils.async.factory.DefaultThreadFactory;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.net.InetSocketAddress;

/**
 * Created by qiunet.
 * 17/11/11
 */
public class NettyHttpServer implements INettyServer {
	private final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	private ChannelFuture closeFuture;
	private final HttpBootstrapParams params;
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

			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(boss, worker);

			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.childAttr(ServerConstants.PROTOCOL_HEADER_ADAPTER, params.getProtocolHeaderType());
			bootstrap.childHandler(new NettyHttpServerInitializer(params));
			bootstrap.option(ChannelOption.SO_REUSEADDR, true);
			bootstrap.option(ChannelOption.SO_BACKLOG, 256);
			this.closeFuture = bootstrap.bind(params.getAddress()).sync();
			logger.error("[NettyHttpServer]  Http server {} is started on port [{}]", serverName(), ((InetSocketAddress) params.getAddress()).getPort());
			this.closeFuture.channel().closeFuture().sync();
		}catch (Exception e) {
			logger.error("[NettyHttpServer] Exception: ", e);
			System.exit(1);
		}finally {
			logger.error("[NettyHttpServer] {} is shutdown! ", serverName());
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}

	/***
	 * 停止
	 */
	@Override
	public void shutdown(){
		this.closeFuture.channel().close();
	}

	@Override
	public String serverName() {
		return this.params.getServerName();
	}

	@Override
	public String threadName() {
		return "BootstrapServer-Http Address ["+params.getAddress().toString()+"]";
	}
}
