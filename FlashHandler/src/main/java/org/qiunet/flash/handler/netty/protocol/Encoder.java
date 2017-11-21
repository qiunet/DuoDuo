package org.qiunet.flash.handler.netty.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.qiunet.flash.handler.context.header.MessageContent;
import org.qiunet.flash.handler.context.header.ProtocolHeader;
import org.qiunet.utils.logger.LoggerManager;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.logger.log.QLogger;

/**
 * Created by qiunet.
 * 17/8/13
 */
public class Encoder extends MessageToByteEncoder<MessageContent> {
	private QLogger logger = LoggerManager.getLogger(LoggerType.FLASH_HANDLER);
	@Override
	protected void encode(ChannelHandlerContext ctx, MessageContent msg, ByteBuf out) throws Exception {
		logger.info("[encode] : " + msg.toString());
		ProtocolHeader header = fillProtocolHeader(msg.getProtocolId(), msg.bytes() ,msg.getSequence());
		header.writeToByteBuf(out);
		out.writeBytes(msg.bytes());
	}

	/**
	 * 通过消息的bytes 填充header
	 * @param bytes
	 * @return
	 */
	private ProtocolHeader fillProtocolHeader(int protocolId, byte [] bytes, int sequence) {
		int length = bytes.length;
		int chunkSize = 0;
		int crc = 0;
		return new ProtocolHeader(length, sequence, protocolId, chunkSize, crc);
	}
}
