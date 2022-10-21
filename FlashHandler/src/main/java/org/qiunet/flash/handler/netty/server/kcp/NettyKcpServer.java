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
import org.qiunet.flash.handler.netty.coder.KcpSocketServerDecoder;
import org.qiunet.flash.handler.netty.coder.KcpSocketServerEncoder;
import org.qiunet.flash.handler.netty.server.INettyServer;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.idle.NettyIdleCheckHandler;
import org.qiunet.flash.handler.netty.server.param.KcpBootstrapParams;
import org.qiunet.utils.async.factory.DefaultThreadFactory;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.system.OSUtil;
import org.slf4j.Logger;

import java.util.List;

/***
 * KCP 服务
 * @author qiunet
 * 2022/4/24 15:53
 */
public class NettyKcpServer implements INettyServer {
	private static final EventLoopGroup GROUP = new NioEventLoopGroup(OSUtil.availableProcessors(), new DefaultThreadFactory("netty-kcp-server-event-loop-"));
	private final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();

	private final KcpBootstrapParams params;

	private List<ChannelFuture> channelFutures;

	public NettyKcpServer(KcpBootstrapParams params) {
		this.params = params;
	}

	@Override
	public String serverName() {
		return this.params.getServerName();
	}

	@Override
	public void shutdown() {
		this.channelFutures.forEach(f -> f.channel().close());
	}

	@Override
	public String threadName() {
		return params.getServerName();
	}

	@Override
	public void run() {
		try {
			UkcpServerBootstrap b = new UkcpServerBootstrap();

			b.group(GROUP)
					.channel(UkcpServerChannel.class)
					.option(ChannelOption.SO_RCVBUF, 1024*1024*2)
					.option(ChannelOption.SO_SNDBUF, 1024*1024*2)
					.option(ChannelOption.SO_REUSEADDR, true)
					.childAttr(ServerConstants.PROTOCOL_HEADER, params.getProtocolHeader())
					.childHandler(new ChannelInitializer<UkcpChannel>() {
						@Override
						public void initChannel(UkcpChannel ch) throws Exception {
							ChannelPipeline p = ch.pipeline();
							p.addLast("KcpSocketEncoder", new KcpSocketServerEncoder())
							.addLast("KcpSocketDecoder", new KcpSocketServerDecoder(params.getMaxReceivedLength(), params.isEncryption()))
							.addLast("IdleStateHandler", new IdleStateHandler(params.getReadIdleCheckSeconds(), 0, 0))
							.addLast("NettyIdleCheckHandler", new NettyIdleCheckHandler())
							.addLast("KcpServerHandler", new KcpServerHandler(params));
						}
					});

			ChannelOptionHelper.nodelay(b, params.getKcpParam().noDelay(), params.getKcpParam().interval(), params.getKcpParam().fastResend(), params.getKcpParam().noCwnd())
					.childOption(UkcpChannelOption.UKCP_MTU, params.getKcpParam().mtu())
					.childOption(UkcpChannelOption.UKCP_SND_WND, params.getKcpParam().snd_wnd())
					.childOption(UkcpChannelOption.UKCP_RCV_WND, params.getKcpParam().rcv_wnd())
					.childOption(UkcpChannelOption.UKCP_AUTO_SET_CONV, true);

			this.channelFutures = Lists.newArrayListWithCapacity(this.params.getPorts().size());
			// Start the server.
			for (int port : this.params.getPorts()) {
				ChannelFuture channelFuture = b.bind(port).sync();
				this.channelFutures.add(channelFuture);
			}
			logger.error("[NettyKcpServer]  Kcp server {} is Listener on ports [{}]", serverName(), StringUtil.arraysToString(params.getPorts(), "", "" ,","));
			this.channelFutures.get(0).channel().closeFuture().sync();
		} catch (InterruptedException e) {
			logger.error("[NettyKcpServer] Exception: ", e);
			System.exit(1);
		} finally {
			// Shut down all event loops to terminate all threads.
			logger.error("[NettyKcpServer] {} is shutdown! ", serverName());
			GROUP.shutdownGracefully();
		}
	}
}
