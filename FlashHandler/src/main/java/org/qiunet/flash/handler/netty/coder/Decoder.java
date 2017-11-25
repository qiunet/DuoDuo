package org.qiunet.flash.handler.netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.qiunet.flash.handler.context.header.MessageContent;
import org.qiunet.flash.handler.context.header.ProtocolHeader;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.utils.logger.LoggerManager;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.logger.log.QLogger;

import java.util.Arrays;
import java.util.List;

/**
 * 消息的解析
 * Created by qiunet.
 * 17/8/13
 */
public class Decoder extends ByteToMessageDecoder {
	private QLogger logger = LoggerManager.getLogger(LoggerType.FLASH_HANDLER);
	private int maxReceivedLength;
	public Decoder(int maxReceivedLength) {
		this.maxReceivedLength = maxReceivedLength;
	}
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (! in.isReadable(ProtocolHeader.REQUEST_HEADER_LENGTH)) return;
		in.markReaderIndex();

		ProtocolHeader header = new ProtocolHeader(in);
		if (! header.isMagicValid()) {
			logger.error("Invalid message, magic is error! "+ Arrays.toString(header.getMagic()));
			ctx.channel().close();
			return;
		}

		if (header.getLength() <= 0 || header.getLength() > maxReceivedLength) {
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

		MessageContent context = new MessageContent(header.getProtocolId(), bytes);
		out.add(context);
	}
}
