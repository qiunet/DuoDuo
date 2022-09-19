package org.qiunet.flash.handler.netty.server.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.qiunet.flash.handler.netty.server.INettyServer;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.flash.handler.netty.server.tcp.init.NettyTcpServerInitializer;
import org.qiunet.flash.handler.util.NettyUtil;
import org.qiunet.utils.async.factory.DefaultThreadFactory;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

/**
 * Created by qiunet.
 * 17/8/13
 */
public final class NettyTcpServer implements INettyServer {
	public static final EventLoopGroup BOSS = NettyUtil.newEventLoopGroup(1, "netty-tcp-server-boss-event-loop-");
	private final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();

	private final TcpBootstrapParams params;

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
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(BOSS, ServerConstants.WORKER);

			bootstrap.channel(NettyUtil.serverSocketChannelClass());
			bootstrap.childAttr(ServerConstants.PROTOCOL_HEADER_ADAPTER, params.getProtocolHeaderType());
			bootstrap.childHandler(new NettyTcpServerInitializer(params));

			bootstrap.option(ChannelOption.SO_BACKLOG, 256);
			bootstrap.option(ChannelOption.SO_REUSEADDR, true);
			bootstrap.option(ChannelOption.SO_RCVBUF, 1024*1024*2);

			this.channelFuture = bootstrap.bind(params.getPort());
			logger.error("[NettyTcpServer]  Tcp server {} is Listener on port [{}]", serverName(), params.getPort());
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
		return this.params.getServerName();
	}

	@Override
	public void shutdown(){
		this.channelFuture.channel().close();
	}

	@Override
	public String threadName() {
		return "BootstrapServer-Tcp Address ["+params.getPort()+"]";
	}
}
