package org.qiunet.flash.handler.netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.List;

/**
 * Created by qiunet.
 * 17/8/13
 */
public class WebSocketEncoder extends MessageToMessageEncoder<MessageContent> {
	private Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();

	@Override
	protected void encode(ChannelHandlerContext ctx, MessageContent msg, List<Object> out) throws Exception {
		try {
			// 这里不需要release ByteBuf WebSocketFrame 本身会帮你搞定.
			ByteBuf byteBuf = ChannelUtil.messageContentToByteBuf(msg, ctx.channel());
			out.add(new BinaryWebSocketFrame(byteBuf));
		}catch (Exception e) {
			logger.error("WebSocketEncoder", e);
		}
	}
}
