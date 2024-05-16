package org.qiunet.flash.handler.netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.List;

/***
 *
 * @author qiunet
 * 2024/3/6 11:39
 ***/
abstract class BasicTcpSocketCoder extends MessageToMessageEncoder<IChannelMessage<?>> {
	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	/**
	 * 接收最大长度
	 * encode需要限制 超出需要抛弃
	 */
	protected final int maxDecodeReceivedLength;
	/**
	 * 最大长度的1/2
	 */
	protected final int max_1_2_DecodeReceivedLength;
	/**
	 * 最大长度的1/3
	 */
	protected final int max_1_3_DecodeReceivedLength;

	public BasicTcpSocketCoder(int maxDecodeReceivedLength) {
		this.max_1_2_DecodeReceivedLength = maxDecodeReceivedLength / 2;
		this.max_1_3_DecodeReceivedLength = maxDecodeReceivedLength / 3;
		this.maxDecodeReceivedLength = maxDecodeReceivedLength;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, IChannelMessage<?> msg, List<Object> out) throws Exception {
		int protocolID = msg.getProtocolID();
		ByteBuf byteBuf = this.buildByteBuf(ctx.channel(), msg);
		// 头的长度占比不大  就粗略估算了
		int length = byteBuf.readableBytes() - 16;
		if (length > this.maxDecodeReceivedLength) {
			logger.error("Encode protocolID: {} length great than max exception:", protocolID, new RuntimeException("length:" + length));
			byteBuf.release();
			return;
		}

		if (length > this.max_1_2_DecodeReceivedLength) {
			// 抛出异常  但是不影响
			logger.error("Encode protocolID: {} length warning exception:", protocolID, new RuntimeException("warning length:" + length));
		}else if (length > this.max_1_3_DecodeReceivedLength){
			// 抛出警告 需要查证了.
			logger.error("Encode protocolID: {}  length: {} warning!", protocolID, length);
		}

		out.add(byteBuf);
	}

	protected ByteBuf buildByteBuf(Channel channel, IChannelMessage<?> msg) {
		return msg.withHeaderByteBuf(channel);
	}
}
