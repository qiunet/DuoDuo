package org.qiunet.flash.handler.netty.server.kcp;

import com.google.common.collect.Lists;
import io.jpower.kcp.netty.ChannelOptionHelper;
import io.jpower.kcp.netty.UkcpChannel;
import io.jpower.kcp.netty.UkcpChannelOption;
import io.jpower.kcp.netty.UkcpServerChannel;
import io.netty.bootstrap.UkcpServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.Promise;
import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.netty.coder.KcpSocketServerDecoder;
import org.qiunet.flash.handler.netty.coder.KcpSocketServerEncoder;
import org.qiunet.flash.handler.netty.server.INettyServer;
import org.qiunet.flash.handler.netty.server.bound.FlushBalanceHandler;
import org.qiunet.flash.handler.netty.server.bound.MessageReadHandler;
import org.qiunet.flash.handler.netty.server.bound.NettyCauseHandler;
import org.qiunet.flash.handler.netty.server.bound.NettyIdleCheckHandler;
import org.qiunet.flash.handler.netty.server.config.ServerBootStrapConfig;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.utils.async.factory.DefaultThreadFactory;
import org.qiunet.utils.async.future.DNettyPromise;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.system.OSUtil;
import org.slf4j.Logger;

import java.util.List;
import java.util.Set;

/***
 * KCP 服务
 * @author qiunet
 * 2022/4/24 15:53
 */
public class NettyKcpServer implements INettyServer {
	private static final EventLoopGroup GROUP = new NioEventLoopGroup(OSUtil.availableProcessors(), new DefaultThreadFactory("netty-kcp-server-event-loop-"));
	private final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();

	private final Promise<Void> successFuture = new DNettyPromise<>();

	private final ServerBootStrapConfig config;

	private List<ChannelFuture> channelFutures;

	public NettyKcpServer(ServerBootStrapConfig config) {
		this.config = config;
	}

	@Override
	public String serverName() {
		return this.config.getServerName();
	}

	@Override
	public void shutdown() {
		this.channelFutures.forEach(f -> f.channel().close());
	}

	@Override
	public String threadName() {
		return config.getServerName();
	}

	@Override
	public void run() {
		try {
			ServerBootStrapConfig.KcpBootstrapConfig.KcpParam kcpParameter = this.config.getKcpBootstrapConfig().getKcpParam();
			UkcpServerBootstrap b = new UkcpServerBootstrap();
			int buffSize = kcpParameter.mtu() * kcpParameter.rcv_wnd();
			b.group(GROUP)
					.channel(UkcpServerChannel.class)
					// 发送窗口 * Mtu
					.option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(buffSize))
					.option(ChannelOption.SO_RCVBUF, buffSize)
					.option(ChannelOption.SO_SNDBUF, buffSize)
					.option(ChannelOption.SO_REUSEADDR, true)
					.childAttr(ServerConstants.PROTOCOL_HEADER, config.getProtocolHeader())
					.childAttr(ServerConstants.HANDLER_TYPE_KEY, ServerConnType.KCP)
					.childAttr(ServerConstants.BOOTSTRAP_CONFIG_KEY, config)
					.childHandler(new ChannelInitializer<UkcpChannel>() {
						@Override
						public void initChannel(UkcpChannel ch) throws Exception {
							ChannelPipeline p = ch.pipeline();
							p.addLast("KcpSocketEncoder", new KcpSocketServerEncoder())
							.addLast("KcpSocketDecoder", new KcpSocketServerDecoder(config.getMaxReceivedLength(), config.isEncryption()))
							.addLast("IdleStateHandler", new IdleStateHandler(config.getReadIdleCheckSeconds(), 0, 0))
							.addLast("NettyIdleCheckHandler", new NettyIdleCheckHandler())
							.addLast("KcpServerHandler", new KcpServerHandler())
							.addLast("MessageReadHandler", new MessageReadHandler())
							.addLast("FlushBalanceHandler", new FlushBalanceHandler())
							.addLast("NettyCauseHandler", new NettyCauseHandler());
						}
					});
			ChannelOptionHelper.nodelay(b, kcpParameter.noDelay(), kcpParameter.interval(), kcpParameter.fastResend(), kcpParameter.noCwnd())
					.childOption(UkcpChannelOption.UKCP_MTU, kcpParameter.mtu())
					.childOption(UkcpChannelOption.UKCP_SND_WND, kcpParameter.snd_wnd())
					.childOption(UkcpChannelOption.UKCP_RCV_WND, kcpParameter.rcv_wnd())
					.childOption(UkcpChannelOption.UKCP_AUTO_SET_CONV, true);

			Set<Integer> udpPorts = this.config.getKcpBootstrapConfig().getPorts();
			this.channelFutures = Lists.newArrayListWithCapacity(udpPorts.size());
			// Start the server.
			for (int port : udpPorts) {
				ChannelFuture channelFuture = b.bind(port).sync();
				this.channelFutures.add(channelFuture);
			}
			logger.error("[NettyKcpServer]  Kcp server {} is Listener on ports [{}]", serverName(), StringUtil.arraysToString(udpPorts, "", "" ,","));
			// 同步的. 到这步, 是成功过. 否则会到异常那.
			this.successFuture.trySuccess(null);
			this.channelFutures.get(0).channel().closeFuture().sync();
		} catch (Throwable e) {
			this.successFuture.tryFailure(new RuntimeException("!!! [NettyKcpServer] start failed!", e));
		} finally {
			// Shut down all event loops to terminate all threads.
			logger.error("[NettyKcpServer] {} is shutdown! ", serverName());
			GROUP.shutdownGracefully();
		}
	}

	@Override
	public Promise<Void> successFuture() {
		return successFuture;
	}
}
