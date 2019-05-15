package org.qiunet.flash.handler.netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by qiunet.
 * 17/8/13
 */
public class WebSocketEncoder extends MessageToByteEncoder<MessageContent> {
	private Logger logger = LoggerType.DUODUO.getLogger();
	@Override
	protected void encode(ChannelHandlerContext ctx, MessageContent msg, ByteBuf out) throws Exception {
		try {
			// 这里不需要release ByteBuf WebSocketFrame 本身会帮你搞定.
			ctx.write(new BinaryWebSocketFrame(msg.encodeToByteBuf()));
		}catch (Exception e) {
			throw e;
		}
	}
}
