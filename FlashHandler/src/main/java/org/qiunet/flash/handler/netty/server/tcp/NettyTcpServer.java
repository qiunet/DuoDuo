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
			bootstrap.group(BOSS, ServerConstants.WORKER);

			bootstrap.channel(NettyUtil.serverSocketChannelClass());
			bootstrap.childAttr(ServerConstants.PROTOCOL_HEADER, config.getProtocolHeader());
			bootstrap.childHandler(new NettyTcpServerInitializer(config));
			/*
			 * BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，
			 * 用于临时存放已完成三次握手的请求的队列的最大长度。如果未设置或所设置的值小于1，
			 * 使用默认值100
			 */
			bootstrap.option(ChannelOption.SO_BACKLOG, 100);
			/*
			* 表示允许重复使用本地地址和端口
			 */
			bootstrap.option(ChannelOption.SO_REUSEADDR, true);
			/*
			 * 在TCP/IP协议中，无论发送多少数据，总是要在数据前面加上协议头，
			 * 同时，对方接收到数据，也需要发送ACK表示确认。
			 * 为了尽可能的利用网络带宽，TCP总是希望尽可能的发送足够大的数据。
			 * 这里就涉及到一个名为Nagle的算法，该算法的目的就是为了尽可能发送大块数据，避免网络中充斥着许多小数据块。
			 */
			bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
			/*
			* 接收缓冲区用于保存网络协议站内收到的数据，直到应用程序读取成功，
			*/
			bootstrap.childOption(ChannelOption.SO_RCVBUF, 1024 * 128);
			/*
			 * 发送缓冲区用于保存发送数据，直到发送成功。
			 */
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
