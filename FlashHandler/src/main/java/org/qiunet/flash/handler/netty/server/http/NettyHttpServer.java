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
import org.qiunet.flash.handler.util.NettyUtil;
import org.qiunet.utils.async.factory.DefaultThreadFactory;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

/**
 * Created by qiunet.
 * 17/11/11
 */
public class NettyHttpServer implements INettyServer {
	public static final EventLoopGroup BOSS = NettyUtil.newEventLoopGroup(1, "netty-http-server-boss-event-loop-");
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
		try {

			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(BOSS, ServerConstants.WORKER);

			bootstrap.channel(NettyUtil.serverSocketChannelClass());
			bootstrap.childAttr(ServerConstants.PROTOCOL_HEADER_ADAPTER, params.getProtocolHeaderType());
			bootstrap.childHandler(new NettyHttpServerInitializer(params));
			bootstrap.option(ChannelOption.SO_REUSEADDR, true);
			bootstrap.option(ChannelOption.SO_BACKLOG, 256);
			this.closeFuture = bootstrap.bind(params.getPort()).sync();
			logger.error("[NettyHttpServer]  Http server {} is started on port [{}]", serverName(), params.getPort());
			this.closeFuture.channel().closeFuture().sync();
		}catch (Exception e) {
			logger.error("[NettyHttpServer] Exception: ", e);
			System.exit(1);
		}finally {
			logger.error("[NettyHttpServer] {} is shutdown! ", serverName());
			BOSS.shutdownGracefully();
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
		return "BootstrapServer-Http Address ["+params.getPort()+"]";
	}
}
