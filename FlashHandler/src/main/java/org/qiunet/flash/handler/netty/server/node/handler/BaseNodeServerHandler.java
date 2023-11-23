package org.qiunet.flash.handler.netty.server.node.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.request.data.ChannelDataMapping;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.netty.server.config.adapter.message.ServerPongResponse;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

/***
 * Node server handler父类
 *
 * @author qiunet
 * 2023/3/27 17:53
 */
public abstract class BaseNodeServerHandler extends SimpleChannelInboundHandler<MessageContent> {
	protected final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MessageContent msg) throws Exception {
		if (msg.getProtocolId() == IProtocolId.System.CLIENT_PING) {
			ctx.writeAndFlush(ServerPongResponse.valueOf(null).buildChannelMessage());
			return;
		}

		IHandler handler = ChannelDataMapping.getHandler(msg.getProtocolId());
		if (handler == null) {
			logger.error("Not IHandler for Protocol id [{}]!", msg.getProtocolId());
			return;
		}

		this.channelRead1(ctx, msg, handler);
	}

	protected abstract void channelRead1(ChannelHandlerContext ctx, MessageContent msg, IHandler handler) throws Exception;
}
