package org.qiunet.flash.handler.netty.server.node;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.util.concurrent.Promise;
import org.qiunet.cross.node.ServerNodeManager;
import org.qiunet.cross.node.ServerNodeServerHandler;
import org.qiunet.flash.handler.context.header.NodeProtocolHeader;
import org.qiunet.flash.handler.netty.coder.TcpSocketServerDecoder;
import org.qiunet.flash.handler.netty.coder.TcpSocketServerEncoder;
import org.qiunet.flash.handler.netty.server.INettyServer;
import org.qiunet.flash.handler.netty.server.bound.FlushBalanceHandler;
import org.qiunet.flash.handler.netty.server.bound.NettyCauseHandler;
import org.qiunet.flash.handler.netty.server.bound.NettyIdleCheckHandler;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.node.handler.CrossPlayerNodeServerHandler;
import org.qiunet.flash.handler.util.NettyUtil;
import org.qiunet.utils.async.future.DNettyPromise;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

/**
 * Created by qiunet.
 * 23/4/24
 */
public final class NettyNodeServer implements INettyServer {
	private static final EventLoopGroup BOSS = NettyUtil.newEventLoopGroup(1, "netty-node-server-boss-event-loop-");
	private final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	private final Promise<Void> successFuture = new DNettyPromise<>();

	/**
	 * 完成了. 调用
	 */
	private ChannelFuture channelFuture;

	private final String serverName;

	private final int port;
	/***
	 * 启动
	 */
	public NettyNodeServer() {
		this.port = ServerNodeManager.getCurrServerInfo().getNodePort();
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
					pipeline.addLast("ServerNodeServerHandler", new ServerNodeServerHandler());
					pipeline.addLast("PlayerNodeServerHandler", new CrossPlayerNodeServerHandler());
					pipeline.addLast("FlushBalanceHandler", new FlushBalanceHandler(50, 10));
					pipeline.addLast("NettyCauseHandler", new NettyCauseHandler());
				}
			});

			bootstrap.option(ChannelOption.SO_BACKLOG, 256);
			bootstrap.option(ChannelOption.SO_REUSEADDR, true);
			bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
			bootstrap.childOption(ChannelOption.SO_RCVBUF, 1024 * 128);
			bootstrap.childOption(ChannelOption.SO_SNDBUF, 1024 * 128);
			this.channelFuture = bootstrap.bind(port).sync();
			this.channelFuture.addListener(channelFuture -> {
				logger.error("[NettyNodeServer]  Tcp server {} is Listener on port [{}]", serverName(), this.port);
				successFuture.trySuccess(null);
			});
			channelFuture.channel().closeFuture().sync();
		}catch (Throwable e) {
			this.successFuture.tryFailure(new RuntimeException("!!! [NettyNodeServer] start failed!", e));
		}finally {
			logger.error("[NettyNodeServer] {} is shutdown! ", serverName());
			BOSS.shutdownGracefully();
		}
	}

	@Override
	public Promise<Void> successFuture() {
		return successFuture;
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
