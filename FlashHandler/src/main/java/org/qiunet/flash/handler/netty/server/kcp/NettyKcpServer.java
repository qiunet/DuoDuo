package org.qiunet.flash.handler.netty.server.kcp;

import io.jpower.kcp.netty.ChannelOptionHelper;
import io.jpower.kcp.netty.UkcpChannel;
import io.jpower.kcp.netty.UkcpChannelOption;
import io.jpower.kcp.netty.UkcpServerChannel;
import io.netty.bootstrap.UkcpServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;
import org.qiunet.flash.handler.netty.coder.KcpSocketDecoder;
import org.qiunet.flash.handler.netty.coder.KcpSocketEncoder;
import org.qiunet.flash.handler.netty.server.INettyServer;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.idle.NettyIdleCheckHandler;
import org.qiunet.flash.handler.netty.server.param.KcpBootstrapParams;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.net.InetSocketAddress;

/***
 * KCP 服务
 * @author qiunet
 * 2022/4/24 15:53
 */
public class NettyKcpServer implements INettyServer {
	private final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();

	private final KcpBootstrapParams params;

	private ChannelFuture channelFuture;

	public NettyKcpServer(KcpBootstrapParams params) {
		this.params = params;
	}

	@Override
	public String serverName() {
		return this.params.getServerName();
	}

	@Override
	public void shutdown() {
		this.channelFuture.channel().close();
	}

	@Override
	public String threadName() {
		return params.getServerName();
	}

	@Override
	public void run() {
		try {
			UkcpServerBootstrap b = new UkcpServerBootstrap();

			b.group(ServerConstants.WORKER)
					.channel(UkcpServerChannel.class)
					.option(ChannelOption.SO_RCVBUF, 1024*1024*2)
					.option(ChannelOption.SO_SNDBUF, 1024*1024*2)
					.childAttr(ServerConstants.PROTOCOL_HEADER_ADAPTER, params.getProtocolHeaderType())
					.childHandler(new ChannelInitializer<UkcpChannel>() {
						@Override
						public void initChannel(UkcpChannel ch) throws Exception {
							ChannelPipeline p = ch.pipeline();
							p.addLast("KcpSocketEncoder", new KcpSocketEncoder())
							.addLast("KcpSocketDecoder", new KcpSocketDecoder(params.getMaxReceivedLength(), params.isEncryption()))
							.addLast("IdleStateHandler", new IdleStateHandler(params.getReadIdleCheckSeconds(), 0, 0))
							.addLast("NettyIdleCheckHandler", new NettyIdleCheckHandler())
							.addLast("KcpServerHandler", new KcpServerHandler(params));
						}
					});

			ChannelOptionHelper.nodelay(b, params.getKcpParam().isNoDelay(), params.getKcpParam().getInterval(), params.getKcpParam().getFastResend(), params.getKcpParam().isNoCwnd())
					.childOption(UkcpChannelOption.UKCP_MTU, params.getKcpParam().getMtu())
					.childOption(UkcpChannelOption.UKCP_SND_WND, params.getKcpParam().getSnd_wnd())
					.childOption(UkcpChannelOption.UKCP_RCV_WND, params.getKcpParam().getRcv_wnd())
					.childOption(UkcpChannelOption.UKCP_AUTO_SET_CONV, true);

			// Start the server.
			this.channelFuture = b.bind(this.params.getAddress()).sync();
			logger.error("[NettyKcpServer]  Kcp server {} is Listener on port [{}]", serverName(), ((InetSocketAddress) params.getAddress()).getPort());
			// Wait until the server socket is closed.
			this.channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			logger.error("[NettyKcpServer] Exception: ", e);
			System.exit(1);
		} finally {
			// Shut down all event loops to terminate all threads.
			logger.error("[NettyKcpServer] {} is shutdown! ", serverName());
		}
	}
}
