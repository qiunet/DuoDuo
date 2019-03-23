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
	private Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);
	@Override
	protected void encode(ChannelHandlerContext ctx, MessageContent msg, ByteBuf out) throws Exception {
		ByteBuf srcMsg = msg.encodeToByteBuf();
		BinaryWebSocketFrame frame = null;
		try {
			frame = new BinaryWebSocketFrame(srcMsg);
			out.writeBytes(frame.content());
		}finally {
			srcMsg.release();
		}
	}
}
