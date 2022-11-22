package org.qiunet.flash.handler.netty.client.kcp;

import io.jpower.kcp.netty.ChannelOptionHelper;
import io.jpower.kcp.netty.UkcpChannel;
import io.jpower.kcp.netty.UkcpChannelOption;
import io.jpower.kcp.netty.UkcpClientChannel;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.session.KcpSession;
import org.qiunet.flash.handler.netty.client.param.KcpClientConfig;
import org.qiunet.flash.handler.netty.client.trigger.IPersistConnResponseTrigger;
import org.qiunet.flash.handler.netty.coder.KcpSocketClientDecoder;
import org.qiunet.flash.handler.netty.coder.KcpSocketClientEncoder;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.utils.async.factory.DefaultThreadFactory;
import org.qiunet.utils.logger.LoggerType;

/***
 *
 * @author qiunet
 * 2022/4/26 11:28
 */
public class NettyKcpClient {
	private static final NioEventLoopGroup group = new NioEventLoopGroup( new DefaultThreadFactory("netty-kcp-client-event-loop-"));
	private final IPersistConnResponseTrigger trigger;
	private final KcpClientConfig config;
	private final Bootstrap bootstrap;

	private NettyKcpClient(KcpClientConfig config, IPersistConnResponseTrigger trigger) {
		this.trigger = trigger;
		this.config = config;

		this.bootstrap = new Bootstrap();
		this.bootstrap.group(group).channel(UkcpClientChannel.class)
				.handler(new ChannelInitializer<UkcpChannel>() {
					@Override
					protected void initChannel(UkcpChannel ch) throws Exception {
						ChannelPipeline p = ch.pipeline();
						ch.attr(ServerConstants.PROTOCOL_HEADER).set(config.getProtocolHeader());
						p.addLast("KcpSocketEncoder", new KcpSocketClientEncoder())
						.addLast("KcpSocketDecoder", new KcpSocketClientDecoder(config.getMaxReceivedLength(), config.isEncryption()))
						.addLast("KcpServerHandler", new NettyClientHandler());
					}
				})
				.option(ChannelOption.SO_RCVBUF, 1024 * 128)
				.option(ChannelOption.SO_SNDBUF, 1024 * 128);

		ChannelOptionHelper.nodelay(bootstrap, true, 20, 2, true)
				.option(UkcpChannelOption.UKCP_MTU, 512)
				.option(UkcpChannelOption.UKCP_SND_WND, 512)
				.option(UkcpChannelOption.UKCP_RCV_WND, 512)
		;
	}

	/**
	 * client
	 * @param config
	 * @param trigger
	 * @return
	 */
	public static NettyKcpClient create(KcpClientConfig config, IPersistConnResponseTrigger trigger) {
		return new NettyKcpClient(config, trigger);
	}

	/**
	 * 连接远程服务器
	 * @param host
	 * @param port
	 * @return
	 */
	public KcpSession connect(String host, int port) {
		// Start the client.
		try {
			ChannelFuture f = this.bootstrap.connect(host, port).sync();
			KcpSession kcpSession = new KcpSession(((UkcpClientChannel) f.channel()).conv(config.getConvId()));
			f.channel().attr(ServerConstants.SESSION_KEY).set(kcpSession);
			return kcpSession;
		} catch (InterruptedException e) {
			LoggerType.DUODUO.error("", e);
		}
		return null;
	}

	private class NettyClientHandler extends SimpleChannelInboundHandler<MessageContent> {

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			ctx.channel().attr(ServerConstants.HANDLER_TYPE_KEY).set(ServerConnType.KCP);
			super.channelActive(ctx);
		}

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, MessageContent msg) throws Exception {
			trigger.response(ctx.channel().attr(ServerConstants.SESSION_KEY).get(), msg);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			LoggerType.DUODUO_FLASH_HANDLER.error("Netty kcp client exception: ", cause);
		}
	}
}
