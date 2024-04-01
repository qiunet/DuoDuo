package org.qiunet.cross.pool;

import io.netty.channel.*;
import io.netty.channel.pool.ChannelPoolHandler;
import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.header.INodeServerHeader;
import org.qiunet.flash.handler.context.header.NodeProtocolHeader;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.netty.coder.TcpSocketClientDecoder;
import org.qiunet.flash.handler.netty.coder.TcpSocketClientEncoder;
import org.qiunet.flash.handler.netty.server.bound.FlushBalanceHandler;
import org.qiunet.flash.handler.netty.server.bound.NettyCauseHandler;
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
	private final ClientChannelPoolHandler clientChannelPoolHandler;

	private final int maxMsgLength;

	public NodeChannelPoolHandler(NodeChannelTrigger channelTrigger, int maxMsgLength) {
		this.clientChannelPoolHandler = new ClientChannelPoolHandler(channelTrigger);
		this.maxMsgLength = maxMsgLength;
	}

	@Override
	public void channelReleased(Channel ch) throws Exception {
		ch.attr(NodeChannelPool.COUNTER_KEY).get().decrementAndGet();
	}

	@Override
	public void channelAcquired(Channel ch) throws Exception {
		ch.attr(NodeChannelPool.COUNTER_KEY).get().incrementAndGet();
	}

	@Override
	public void channelCreated(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		ch.attr(ServerConstants.PROTOCOL_HEADER).set(NodeProtocolHeader.instance);
		ch.attr(ServerConstants.HANDLER_TYPE_KEY).set(ServerConnType.TCP);
		pipeline.addLast("TcpSocketDecoder", new TcpSocketClientDecoder(this.maxMsgLength, false));
		pipeline.addLast("TcpSocketEncoder", new TcpSocketClientEncoder(this.maxMsgLength));
		pipeline.addLast("ClientChannelPoolHandler", clientChannelPoolHandler);
		pipeline.addLast("FlushBalanceHandler", new FlushBalanceHandler(50, 10));
		pipeline.addLast("NettyCauseHandler", new NettyCauseHandler());
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
			INodeServerHeader header = (INodeServerHeader) msg.getHeader();
			if (msg.getProtocolId() == IProtocolId.System.SERVER_PONG || header.id() == 0) {
				return;
			}

			ISession nodeSession = this.channelTrigger.getNodeSession(ctx.channel(), header);
			if (nodeSession == null) {
				LoggerType.DUODUO_FLASH_HANDLER.debug("Session ID {} not exist! Can not handler protocol id [{}]!", header, msg.getProtocolId());
				return;
			}
			this.channelTrigger.response(nodeSession, ctx.channel(), msg);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			LoggerType.DUODUO_FLASH_HANDLER.error("Netty node channel pool exception: ", cause);
		}
	}
}
