package org.qiunet.flash.handler.netty.protocol;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.log4j.Logger;
import org.qiunet.flash.handler.context.header.ProtocolHeader;

/**
 * Created by qiunet.
 * 17/8/13
 */
public class ProtoBufEncoder  extends MessageToByteEncoder<GeneratedMessageV3> {
	private Logger logger = Logger.getLogger(ProtoBufEncoder.class);
	@Override
	protected void encode(ChannelHandlerContext ctx, GeneratedMessageV3 msg, ByteBuf out) throws Exception {
		logger.info("[encode] : " + msg.toString());
		byte bytes [] = (msg).toByteArray();
		ProtocolHeader header = fillProtocolHeader(0, bytes);
		header.writeToByteBuf(out);
		out.writeBytes(bytes);
	}

	/**
	 * 通过消息的bytes 填充header
	 * @param bytes
	 * @return
	 */
	private ProtocolHeader fillProtocolHeader(int protocolId, byte [] bytes) {
		int length = bytes.length;
		int sequence = 0;
		int chunkSize = 0;
		long crc = 0;
		return new ProtocolHeader(length, sequence, protocolId, chunkSize, crc);
	}
}
