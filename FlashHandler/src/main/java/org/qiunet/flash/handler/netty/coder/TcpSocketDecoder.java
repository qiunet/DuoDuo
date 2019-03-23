package org.qiunet.flash.handler.netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.header.ProtocolHeader;
import org.qiunet.utils.encryptAndDecrypt.CrcUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * 消息的解析
 * Created by qiunet.
 * 17/8/13
 */
public class TcpSocketDecoder extends ByteToMessageDecoder {
	private Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);
	private int maxReceivedLength;
	private boolean crc;
	public TcpSocketDecoder(int maxReceivedLength, boolean needCrc) {
		this.crc = needCrc;
		this.maxReceivedLength = maxReceivedLength;
	}
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		ProtocolHeader header = new ProtocolHeader();
		if (! in.isReadable(header.getHeaderLength())) return;
		in.markReaderIndex();

		header.parseHeader(in);
		if (! header.isMagicValid()) {
			logger.error("Invalid message, magic is error! "+ header);
			ctx.channel().close();
			return;
		}

		if (header.getLength() < 0 || header.getLength() > maxReceivedLength) {
			logger.error("Invalid message, length is error! length is : "+ header.getLength());
			ctx.channel().close();
			return;
		}

		if (! in.isReadable(header.getLength())) {
			in.resetReaderIndex();
			return;
		}
		byte [] bytes = new byte[header.getLength()];
		in.readBytes(bytes);

		if (crc && ! header.encryptionValid(CrcUtil.getCrc32Value(bytes))) {
			logger.error("Invalid message encryption! server is : "+ CrcUtil.getCrc32Value(bytes) +" client is "+header);
			ctx.channel().close();
			return;
		}

		MessageContent context = new MessageContent(header.getProtocolId(), bytes);
		out.add(context);
	}
}
