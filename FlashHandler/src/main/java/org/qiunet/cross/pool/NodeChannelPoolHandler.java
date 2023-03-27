package org.qiunet.cross.pool;

import io.netty.channel.*;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.ScheduledFuture;
import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.context.header.NodeProtocolHeader;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.netty.coder.TcpSocketClientDecoder;
import org.qiunet.flash.handler.netty.coder.TcpSocketClientEncoder;
import org.qiunet.flash.handler.netty.handler.FlushBalanceHandler;
import org.qiunet.flash.handler.netty.server.config.adapter.message.ClientPingRequest;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.utils.logger.LoggerType;

import java.util.concurrent.TimeUnit;

/***
 * Node channel pool 的channel 创建处理
 *
 * @author qiunet
 * 2023/3/31 13:42
 */
public class NodeChannelPoolHandler implements ChannelPoolHandler {
	private static final AttributeKey<ScheduledFuture<?>> CLOSE_CHANNEL_FUTURE = AttributeKey.newInstance("org.qiunet.cross.pool.NodeChannelPoolHandler.close.channel");
	private final ClientChannelPoolHandler clientChannelPoolHandler;

	private final int maxReceivedLength;

	public NodeChannelPoolHandler(NodeChannelTrigger channelTrigger, int maxReceivedLength) {
		this.clientChannelPoolHandler = new ClientChannelPoolHandler(channelTrigger);
		this.maxReceivedLength = maxReceivedLength;
	}

	@Override
	public void channelReleased(Channel ch) throws Exception {
		int num = ch.attr(NodeChannelPool.COUNTER_KEY).get().decrementAndGet();
		if (num <= 0 && ch.isActive()) {
			ScheduledFuture<?> future = ch.eventLoop().schedule(new Runnable() {
				@Override
				public void run() {
					if (ch.attr(NodeChannelPool.COUNTER_KEY).get().get() > 0) {
						return;
					}
					ch.close();
				}
			}, 60, TimeUnit.SECONDS);
			ch.attr(CLOSE_CHANNEL_FUTURE).set(future);
		}
	}

	@Override
	public void channelAcquired(Channel ch) throws Exception {
		ch.attr(NodeChannelPool.COUNTER_KEY).get().incrementAndGet();
		if (ch.hasAttr(CLOSE_CHANNEL_FUTURE)) {
			ch.attr(CLOSE_CHANNEL_FUTURE).getAndSet(null).cancel(false);
		}
	}

	@Override
	public void channelCreated(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		ch.attr(ServerConstants.PROTOCOL_HEADER).set(NodeProtocolHeader.instance);
		ch.attr(ServerConstants.HANDLER_TYPE_KEY).set(ServerConnType.TCP);
		pipeline.addLast("TcpSocketDecoder", new TcpSocketClientDecoder(this.maxReceivedLength, false));
		pipeline.addLast("TcpSocketEncoder", new TcpSocketClientEncoder());
		pipeline.addLast("ClientChannelPoolHandler", clientChannelPoolHandler);
		pipeline.addLast("FlushBalanceHandler", new FlushBalanceHandler(50, 10));
		ch.eventLoop().scheduleAtFixedRate(() -> {
			ch.writeAndFlush(ClientPingRequest.valueOf().buildChannelMessage());
		}, 5, 30, TimeUnit.SECONDS);
	}

	/**
	 *
	 */
	@ChannelHandler.Sharable
	static class ClientChannelPoolHandler extends SimpleChannelInboundHandler<MessageContent> {
		private final NodeChannelTrigger channelTrigger;
		public ClientChannelPoolHandler(NodeChannelTrigger channelTrigger) {
			this.channelTrigger = channelTrigger;
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			ctx.channel().attr(ServerConstants.HANDLER_TYPE_KEY).set(ServerConnType.TCP);
			super.channelActive(ctx);
		}

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, MessageContent msg) throws Exception {
			IProtocolHeader.INodeServerHeader header = (IProtocolHeader.INodeServerHeader) msg.getHeader();
			if (msg.getProtocolId() == IProtocolId.System.SERVER_PONG || header.id() == 0) {
				return;
			}

			ISession nodeSession = this.channelTrigger.getNodeSession(ctx.channel(), header);
			this.channelTrigger.response(nodeSession, ctx.channel(), msg);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			LoggerType.DUODUO_FLASH_HANDLER.error("Netty node channel pool exception: ", cause);
		}
	}
}
