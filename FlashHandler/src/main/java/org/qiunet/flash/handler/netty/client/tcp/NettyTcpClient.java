package org.qiunet.flash.handler.netty.client.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.client.param.TcpClientParams;
import org.qiunet.flash.handler.netty.client.trigger.IPersistConnResponseTrigger;
import org.qiunet.flash.handler.netty.coder.TcpSocketDecoder;
import org.qiunet.flash.handler.netty.coder.TcpSocketEncoder;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.utils.async.factory.DefaultThreadFactory;

/**
 * Created by qiunet.
 * 17/11/25
 */
public class NettyTcpClient {
	private static final NioEventLoopGroup group = new NioEventLoopGroup(1, new DefaultThreadFactory("netty-tcp-client-event-loop-"));
	private IPersistConnResponseTrigger trigger;
	private TcpClientParams params;
	private Bootstrap bootstrap;
	/**
	 *
	 * @param params
	 */
	private NettyTcpClient(TcpClientParams params, IPersistConnResponseTrigger trigger) {
		this.bootstrap = new Bootstrap();

		this.bootstrap.option(ChannelOption.TCP_NODELAY,true);
		this.bootstrap.handler(new NettyClientInitializer());
		this.bootstrap.channel(NioSocketChannel.class);
		this.bootstrap.group(group);
		this.trigger = trigger;
		this.params = params;
	}

	/**
	 * 阻塞 直到连接成功后返回.
	 * @param params
	 * @return
	 */
	public static NettyTcpClient create(TcpClientParams params, IPersistConnResponseTrigger trigger) {
		return new NettyTcpClient(params, trigger);
	}

	public TcpClientConnector connect(String host, int port) {
		return new TcpClientConnector(bootstrap, host, port);
	}

	public static void shutdown(){
		if (! group.isShutdown()) group.shutdownGracefully();
	}

	static final AttributeKey<DSession> SESSION = AttributeKey.newInstance("CLIENT_SESSION");
	private class NettyClientInitializer extends ChannelInitializer<SocketChannel> {

		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
			ch.attr(ServerConstants.PROTOCOL_HEADER_ADAPTER).set(params.getProtocolHeaderAdapter());
			pipeline.addLast("TcpSocketEncoder", new TcpSocketEncoder());
			pipeline.addLast("TcpSocketDecoder", new TcpSocketDecoder(params.getMaxReceivedLength(), params.isEncryption()));
			pipeline.addLast(new NettyClientHandler());
		}
	}

	private class NettyClientHandler extends SimpleChannelInboundHandler<MessageContent> {
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, MessageContent msg) throws Exception {
			trigger.response(ctx.channel().attr(SESSION).get(), msg);
		}
	}
}
