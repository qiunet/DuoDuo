package org.qiunet.flash.handler.netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.context.header.IProtocolHeaderType;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.List;

/**
 * 消息的解析
 * Created by qiunet.
 * 17/8/13
 */
public class TcpSocketDecoder extends ByteToMessageDecoder {
	private final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	private final int maxReceivedLength;
	private final boolean encryption;
	public TcpSocketDecoder(int maxReceivedLength, boolean encryption) {
		this.encryption = encryption;
		this.maxReceivedLength = maxReceivedLength;
	}
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (! ctx.channel().isActive()) {
			return;
		}

		IProtocolHeaderType adapter = ChannelUtil.getProtocolHeaderAdapter(ctx.channel());
		if (! in.isReadable(adapter.getReqHeaderLength())) return;
		in.markReaderIndex();

		// header里面的问题 不打印错误信息了. 仅仅本地调试时候打印
		IProtocolHeader header = adapter.inHeader(in, ctx.channel());
		if (! header.isMagicValid()) {
			logger.error("Invalid message, magic is error! {}", header);
			ctx.channel().close();
			return;
		}

		if (header.getLength() < 0 || header.getLength() > maxReceivedLength) {
			logger.error("Invalid message, length is error! length is : {}",  header.getLength());
			ctx.channel().close();
			return;
		}

		if (! in.isReadable(header.getLength())) {
			in.resetReaderIndex();
			return;
		}

		MessageContent content = new MessageContent(header.getProtocolId(), in.readRetainedSlice(header.getLength()));
		if (encryption && ! header.validEncryption(content.byteBuffer())) {
			ctx.channel().close();
			return;
		}

		out.add(content);
	}
}
