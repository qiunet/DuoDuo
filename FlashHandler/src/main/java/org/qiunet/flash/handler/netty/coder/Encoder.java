package org.qiunet.flash.handler.netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by qiunet.
 * 17/8/13
 */
public class Encoder extends MessageToByteEncoder<MessageContent> {
	private Logger logger = LoggerFactory.getLogger(LoggerType.FLASH_HANDLER);
	@Override
	protected void encode(ChannelHandlerContext ctx, MessageContent msg, ByteBuf out) throws Exception {
		ByteBuf srcMsg = msg.encodeToByteBuf();
		try {
			out.writeBytes(srcMsg);
		}finally {
			srcMsg.release();
		}
	}
}
