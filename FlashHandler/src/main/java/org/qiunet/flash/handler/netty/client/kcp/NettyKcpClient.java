package org.qiunet.flash.handler.netty.client.kcp;

import io.jpower.kcp.netty.ChannelOptionHelper;
import io.jpower.kcp.netty.UkcpChannel;
import io.jpower.kcp.netty.UkcpChannelOption;
import io.jpower.kcp.netty.UkcpClientChannel;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.session.KcpSession;
import org.qiunet.flash.handler.netty.client.param.KcpClientParams;
import org.qiunet.flash.handler.netty.client.trigger.IPersistConnResponseTrigger;
import org.qiunet.flash.handler.netty.coder.KcpSocketDecoder;
import org.qiunet.flash.handler.netty.coder.KcpSocketEncoder;
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
	private final KcpClientParams params;
	private final Bootstrap bootstrap;

	private NettyKcpClient(KcpClientParams params, IPersistConnResponseTrigger trigger) {
		this.trigger = trigger;
		this.params = params;

		this.bootstrap = new Bootstrap();
		this.bootstrap.group(group).channel(UkcpClientChannel.class)
				.handler(new ChannelInitializer<UkcpChannel>() {
					@Override
					protected void initChannel(UkcpChannel ch) throws Exception {
						ChannelPipeline p = ch.pipeline();
						ch.attr(ServerConstants.PROTOCOL_HEADER_ADAPTER).set(params.getProtocolHeaderType());
						p.addLast("KcpSocketEncoder", new KcpSocketEncoder())
						.addLast("KcpSocketDecoder", new KcpSocketDecoder(params.getMaxReceivedLength(), params.isEncryption()))
						.addLast("KcpServerHandler", new NettyClientHandler());
					}
				});
		ChannelOptionHelper.nodelay(bootstrap, true, 20, 2, true)
				.option(UkcpChannelOption.UKCP_MTU, 512)
				.option(UkcpChannelOption.UKCP_SND_WND, 128)
				.option(UkcpChannelOption.UKCP_RCV_WND, 128)
		;
	}

	/**
	 * client
	 * @param params
	 * @param trigger
	 * @return
	 */
	public static NettyKcpClient create(KcpClientParams params, IPersistConnResponseTrigger trigger) {
		return new NettyKcpClient(params, trigger);
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
			return new KcpSession(f.channel());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	private class NettyClientHandler extends SimpleChannelInboundHandler<MessageContent> {
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
