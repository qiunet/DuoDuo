package org.qiunet.flash.handler.netty.server.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import org.qiunet.flash.handler.netty.server.INettyServer;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.param.ServerBootStrapParam;
import org.qiunet.flash.handler.netty.server.tcp.init.NettyTcpServerInitializer;
import org.qiunet.flash.handler.util.NettyUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

/**
 * Created by qiunet.
 * 17/8/13
 */
public final class NettyTcpServer implements INettyServer {
	public static final EventLoopGroup BOSS = NettyUtil.newEventLoopGroup(1, "netty-tcp-server-boss-event-loop-");
	private final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();

	private final ServerBootStrapParam param;

	private ChannelFuture channelFuture;
	/***
	 * 启动
	 * @param param  启动使用的端口等启动参数
	 */
	public NettyTcpServer(ServerBootStrapParam param) {
		this.param = param;
	}

	@Override
	public void run() {
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(BOSS, ServerConstants.WORKER);

			bootstrap.channel(NettyUtil.serverSocketChannelClass());
			bootstrap.childAttr(ServerConstants.PROTOCOL_HEADER, param.getProtocolHeader());
			bootstrap.childHandler(new NettyTcpServerInitializer(param));

			bootstrap.option(ChannelOption.SO_BACKLOG, 256);
			bootstrap.option(ChannelOption.SO_REUSEADDR, true);
			bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
			bootstrap.childOption(ChannelOption.SO_RCVBUF, 1024 * 128);
			bootstrap.childOption(ChannelOption.SO_SNDBUF, 1024 * 128);
			this.channelFuture = bootstrap.bind(param.getPort());
			logger.error("[NettyTcpServer]  Tcp server {} is Listener on port [{}]", serverName(), param.getPort());
			channelFuture.channel().closeFuture().sync();
		}catch (Exception e) {
			logger.error("[NettyTcpServer] Exception: ", e);
			System.exit(1);
		}finally {
			logger.error("[NettyTcpServer] {} is shutdown! ", serverName());
			BOSS.shutdownGracefully();
		}
	}

	@Override
	public String serverName() {
		return this.param.getServerName();
	}

	@Override
	public void shutdown(){
		this.channelFuture.channel().close();
	}

	@Override
	public String threadName() {
		return "BootstrapServer-Tcp Address ["+ param.getPort()+"]";
	}
}
