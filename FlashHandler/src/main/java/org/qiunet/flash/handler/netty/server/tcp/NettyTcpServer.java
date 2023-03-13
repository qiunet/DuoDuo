package org.qiunet.flash.handler.netty.server.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import org.qiunet.flash.handler.netty.server.INettyServer;
import org.qiunet.flash.handler.netty.server.config.ServerBootStrapConfig;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.tcp.init.NettyTcpServerInitializer;
import org.qiunet.flash.handler.util.NettyUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

/**
 * Created by qiunet.
 * 17/8/13
 */
public final class NettyTcpServer implements INettyServer {
	private static final EventLoopGroup WORKER = NettyUtil.newEventLoopGroup(0, "netty-tcp-server-worker-event-loop-");
	private static final EventLoopGroup BOSS = NettyUtil.newEventLoopGroup(1, "netty-tcp-server-boss-event-loop-");
	private final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();

	private final ServerBootStrapConfig config;
	/**
	 * 完成了. 调用
	 */
	private final Runnable completeRunner;

	private ChannelFuture channelFuture;
	/***
	 * 启动
	 * @param config  启动使用的端口等启动参数
	 */
	public NettyTcpServer(ServerBootStrapConfig config, Runnable completeRunner) {
		this.completeRunner = completeRunner;
		this.config = config;
	}

	@Override
	public void run() {
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(BOSS, WORKER);

			bootstrap.channel(NettyUtil.serverSocketChannelClass());
			bootstrap.childAttr(ServerConstants.PROTOCOL_HEADER, config.getProtocolHeader());
			bootstrap.childHandler(new NettyTcpServerInitializer(config));

			bootstrap.option(ChannelOption.SO_BACKLOG, 256);
			bootstrap.option(ChannelOption.SO_REUSEADDR, true);
			bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
			bootstrap.childOption(ChannelOption.SO_RCVBUF, 1024 * 128);
			bootstrap.childOption(ChannelOption.SO_SNDBUF, 1024 * 128);
			this.channelFuture = bootstrap.bind(config.getPort()).addListener(future -> {
				if (future.cause() != null) {
					logger.error("[NettyTcpServer] === Tcp server {} fail to listener! ===", serverName());
					return;
				}

				if (future.isSuccess()) {
					logger.error("[NettyTcpServer]  Tcp server {} is Listener on port [{}]", serverName(), config.getPort());
					completeRunner.run();
				}
			});

			channelFuture.channel().closeFuture().sync();
		}catch (Throwable e) {
			logger.error("[NettyTcpServer] Exception: ", e);
			System.exit(1);
		}finally {
			logger.error("[NettyTcpServer] {} is shutdown! ", serverName());
			WORKER.shutdownGracefully();
			BOSS.shutdownGracefully();
		}
	}

	@Override
	public String serverName() {
		return this.config.getServerName();
	}

	@Override
	public void shutdown(){
		this.channelFuture.channel().close();
	}

	@Override
	public String threadName() {
		return "BootstrapServer-Tcp Address ["+ config.getPort()+"]";
	}
}
