package org.qiunet.flash.handler.netty.client.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.netty.client.ILongConnClient;
import org.qiunet.flash.handler.netty.client.trigger.ILongConnResponseTrigger;
import org.qiunet.flash.handler.netty.server.udp.handler.UdpChannel;
import org.qiunet.utils.asyncQuene.factory.DefaultThreadFactory;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.timer.AsyncTimerTask;
import org.qiunet.utils.timer.TimerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/10/9 10:25
 **/
public class NettyUdpClient implements ILongConnClient {
	private static final NioEventLoopGroup group = new NioEventLoopGroup(0, new DefaultThreadFactory("netty-udp-client-event-loop-"));
	private static AtomicInteger clientIdCreator = new AtomicInteger();
	private Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);
	private ChannelHandlerContext channelHandlerContext;
	private ILongConnResponseTrigger trigger;
	private UdpChannel udpChannel;

	public NettyUdpClient(InetSocketAddress address, ILongConnResponseTrigger trigger, boolean crc) {
		this.trigger = trigger;

		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group).channel(NioDatagramChannel.class)
				.option(ChannelOption.SO_BROADCAST, true)
				.handler(new NettyClientInitializer());

		try {
			Channel channel = bootstrap.connect(address).sync().channel();
			this.udpChannel = new UdpChannel(channel, address, crc, clientIdCreator.incrementAndGet());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		TimerManager.getInstance().scheduleAtFixedRate(new AsyncTimerTask() {
			@Override
			protected void asyncRun() {
				udpChannel.timeoutHandler();
			}
		}, 200, 200);
	}

	@Override
	public void sendMessage(MessageContent content) {
		this.udpChannel.sendMessage(content.encodeToByteBuf());
	}

	@Override
	public void close() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		channelHandlerContext.channel().close();
	}

	private class NettyClientInitializer extends ChannelInitializer<NioDatagramChannel> {
		@Override
		protected void initChannel(NioDatagramChannel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
			pipeline.addLast(new NettyClientHandler());
		}
	}

	private class NettyClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {
		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			channelHandlerContext = ctx;
		}

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
			MessageContent content = udpChannel.decodeMessage(msg);
			if (content == null) return;

			trigger.response(content);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			logger.info("Exception", cause);
		}
	}
}
