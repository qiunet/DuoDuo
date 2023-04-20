package org.qiunet.flash.handler.netty.server.node;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import org.qiunet.cross.node.ServerNodeManager;
import org.qiunet.cross.node.ServerNodeServerHandler;
import org.qiunet.flash.handler.context.header.NodeProtocolHeader;
import org.qiunet.flash.handler.netty.coder.TcpSocketServerDecoder;
import org.qiunet.flash.handler.netty.coder.TcpSocketServerEncoder;
import org.qiunet.flash.handler.netty.server.INettyServer;
import org.qiunet.flash.handler.netty.server.bound.FlushBalanceHandler;
import org.qiunet.flash.handler.netty.server.bound.NettyIdleCheckHandler;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.node.handler.PlayerNodeServerHandler;
import org.qiunet.flash.handler.util.NettyUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

/**
 * Created by qiunet.
 * 23/4/24
 */
public final class NettyNodeServer implements INettyServer {
	private static final EventLoopGroup BOSS = NettyUtil.newEventLoopGroup(1, "netty-node-server-boss-event-loop-");
	private static final ServerNodeServerHandler serverNodeServerHandler = new ServerNodeServerHandler();
	private static final PlayerNodeServerHandler nodeServerHandler = new PlayerNodeServerHandler();
	private final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();

	/**
	 * 完成了. 调用
	 */
	private final Runnable completeRunner;

	private ChannelFuture channelFuture;

	private final String serverName;

	private final int port;
	/***
	 * 启动
	 */
	public NettyNodeServer(Runnable completeRunner) {
		this.port = ServerNodeManager.getCurrServerInfo().getNodePort();
		this.completeRunner = completeRunner;
		this.serverName = "Node Server";
	}

	@Override
	public void run() {
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(BOSS, ServerConstants.WORKER);

			bootstrap.childAttr(ServerConstants.PROTOCOL_HEADER, NodeProtocolHeader.instance);
			bootstrap.channel(NettyUtil.serverSocketChannelClass());
			bootstrap.childHandler(new ChannelInitializer<>() {
				@Override
				protected void initChannel(Channel ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();

					pipeline.addLast("TcpSocketEncoder", new TcpSocketServerEncoder());
					pipeline.addLast("TcpSocketDecoder", new TcpSocketServerDecoder(8192, false));
					pipeline.addLast("NettyIdleCheckHandler", new NettyIdleCheckHandler());
					pipeline.addLast("ServerNodeServerHandler", serverNodeServerHandler);
					pipeline.addLast("PlayerNodeServerHandler", nodeServerHandler);
					pipeline.addLast("FlushBalanceHandler", new FlushBalanceHandler(50, 10));
				}
			});

			bootstrap.option(ChannelOption.SO_BACKLOG, 256);
			bootstrap.option(ChannelOption.SO_REUSEADDR, true);
			bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
			bootstrap.childOption(ChannelOption.SO_RCVBUF, 1024 * 128);
			bootstrap.childOption(ChannelOption.SO_SNDBUF, 1024 * 128);
			this.channelFuture = bootstrap.bind(port).addListener(future -> {
				if (future.cause() != null) {
					logger.error("[NettyNodeServer] === node server {} fail to listener! ===", serverName());
					return;
				}

				if (future.isSuccess()) {
					logger.error("[NettyNodeServer]  node server {} is Listener on port [{}]", serverName(), port);
					completeRunner.run();
				}
			});

			channelFuture.channel().closeFuture().sync();
		}catch (Throwable e) {
			logger.error("[NettyNodeServer] Exception: ", e);
			System.exit(1);
		}finally {
			logger.error("[NettyNodeServer] {} is shutdown! ", serverName());
			BOSS.shutdownGracefully();
		}
	}

	@Override
	public String serverName() {
		return serverName;
	}

	@Override
	public void shutdown(){
		this.channelFuture.channel().close();
	}

	@Override
	public String threadName() {
		return "BootstrapServer-Node-Server Address ["+ port+"]";
	}
}
