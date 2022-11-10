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
import org.qiunet.flash.handler.netty.server.param.ServerBootStrapParam;
import org.qiunet.utils.async.factory.DefaultThreadFactory;
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

	private final ServerBootStrapParam param;

	private List<ChannelFuture> channelFutures;

	public NettyKcpServer(ServerBootStrapParam param) {
		this.param = param;
	}

	@Override
	public String serverName() {
		return this.param.getServerName();
	}

	@Override
	public void shutdown() {
		this.channelFutures.forEach(f -> f.channel().close());
	}

	@Override
	public String threadName() {
		return param.getServerName();
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
					.childAttr(ServerConstants.PROTOCOL_HEADER, param.getProtocolHeader())
					.childHandler(new ChannelInitializer<UkcpChannel>() {
						@Override
						public void initChannel(UkcpChannel ch) throws Exception {
							ChannelPipeline p = ch.pipeline();
							p.addLast("KcpSocketEncoder", new KcpSocketServerEncoder())
							.addLast("KcpSocketDecoder", new KcpSocketServerDecoder(param.getMaxReceivedLength(), param.isEncryption()))
							.addLast("IdleStateHandler", new IdleStateHandler(param.getReadIdleCheckSeconds(), 0, 0))
							.addLast("NettyIdleCheckHandler", new NettyIdleCheckHandler())
							.addLast("KcpServerHandler", new KcpServerHandler(param));
						}
					});
			ServerBootStrapParam.KcpBootstrapParam.Param param = this.param.getKcpParam().getParam();
			ChannelOptionHelper.nodelay(b, param.noDelay(), param.interval(), param.fastResend(), param.noCwnd())
					.childOption(UkcpChannelOption.UKCP_MTU, param.mtu())
					.childOption(UkcpChannelOption.UKCP_SND_WND, param.snd_wnd())
					.childOption(UkcpChannelOption.UKCP_RCV_WND, param.rcv_wnd())
					.childOption(UkcpChannelOption.UKCP_AUTO_SET_CONV, true);

			Set<Integer> udpPorts = this.param.getKcpParam().getPorts();
			this.channelFutures = Lists.newArrayListWithCapacity(udpPorts.size());
			// Start the server.
			for (int port : udpPorts) {
				ChannelFuture channelFuture = b.bind(port).sync();
				this.channelFutures.add(channelFuture);
			}
			logger.error("[NettyKcpServer]  Kcp server {} is Listener on ports [{}]", serverName(), StringUtil.arraysToString(udpPorts, "", "" ,","));
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
