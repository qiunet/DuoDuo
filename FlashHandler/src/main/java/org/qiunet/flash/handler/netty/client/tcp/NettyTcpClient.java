package org.qiunet.flash.handler.netty.client.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.client.ILongConnClient;
import org.qiunet.flash.handler.netty.client.param.TcpClientParams;
import org.qiunet.flash.handler.netty.client.trigger.ILongConnResponseTrigger;
import org.qiunet.flash.handler.netty.coder.TcpSocketDecoder;
import org.qiunet.flash.handler.netty.coder.TcpSocketEncoder;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.utils.async.factory.DefaultThreadFactory;
import org.qiunet.utils.async.future.DCompletePromise;
import org.qiunet.utils.async.future.DPromise;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

/**
 * Created by qiunet.
 * 17/11/25
 */
public class NettyTcpClient implements ILongConnClient {
	private static final NioEventLoopGroup group = new NioEventLoopGroup(1, new DefaultThreadFactory("netty-tcp-client-event-loop-"));
	private Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	private ILongConnResponseTrigger trigger;
	private Channel channel;
	private DSession session;

	/**
	 *
	 * @param params
	 * @param trigger
	 */
	private NettyTcpClient(TcpClientParams params, ILongConnResponseTrigger trigger, DPromise<NettyTcpClient> promise) {
		this.trigger = trigger;
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group);

		bootstrap.channel(NioSocketChannel.class);
		bootstrap.option(ChannelOption.TCP_NODELAY,true);
		bootstrap.handler(new NettyClientInitializer(params));
		GenericFutureListener<ChannelFuture> listener = f -> {
			if (f.isSuccess()) {
				this.channel = f.channel();
				this.session = new DSession(channel);
				promise.trySuccess(this);
			}else {
				promise.tryFailure(new CustomException("Tcp Connect fail!"));
			}
		};

		ChannelFuture channelFuture = bootstrap.connect(params.getAddress());
		channelFuture.addListener(listener);
	}

	/**
	 * 阻塞 直到连接成功后返回.
	 * @param params
	 * @param trigger
	 * @return
	 */
	public static NettyTcpClient create(TcpClientParams params, ILongConnResponseTrigger trigger) {
		DPromise<NettyTcpClient> promise = new DCompletePromise<>();
		new NettyTcpClient(params, trigger, promise);
		try {
			return promise.get();
		} catch (Exception e) {
			throw new CustomException(e, "TCP CONNECT ERROR!");
		}
	}

	@Override
	public void sendMessage(MessageContent content){
		channel.writeAndFlush(content);
	}

	public DSession getSession() {
		return session;
	}

	public static void shutdown(){
		if (! group.isShutdown()) group.shutdownGracefully();
	}

	private class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
		private TcpClientParams params;

		public NettyClientInitializer(TcpClientParams params) {
			this.params = params;
		}

		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
			ch.attr(ServerConstants.PROTOCOL_HEADER_ADAPTER).set(params.getProtocolHeaderAdapter());
			pipeline.addLast("TcpSocketEncoder", new TcpSocketEncoder());
			pipeline.addLast("TcpSocketDecoder", new TcpSocketDecoder(1024*1024*2, true));
			pipeline.addLast(new NettyClientHandler());
		}
	}

	private class NettyClientHandler extends SimpleChannelInboundHandler<MessageContent> {
		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
		}

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, MessageContent msg) throws Exception {
			trigger.response(session, msg);
		}
	}
}
