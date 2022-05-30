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
public class WebSocketDecoder extends ByteToMessageDecoder {
	private final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	private final int maxReceivedLength;
	private final boolean encryption;
	public WebSocketDecoder(int maxReceivedLength, boolean encryption) {
		this.encryption = encryption;
		this.maxReceivedLength = maxReceivedLength;
	}
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		IProtocolHeaderType adapter = ChannelUtil.getProtocolHeaderAdapter(ctx.channel());
		if (! in.isReadable(adapter.getReqHeaderLength())) {
			// 有的探测包没有这么长. 导致报错.
			return;
		}

		IProtocolHeader header = adapter.inHeader(in, ctx.channel());
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
		ByteBuf byteBuf = in.readRetainedSlice(header.getLength());
		MessageContent context = new MessageContent(header.getProtocolId(), byteBuf);
		if (encryption && !header.validEncryption(context.byteBuffer())) {
			context.release();
			ctx.channel().close();
			return;
		}

		out.add(context);
	}
}
