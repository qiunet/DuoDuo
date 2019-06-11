package org.qiunet.flash.handler.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.context.header.IProtocolHeaderAdapter;
import org.qiunet.flash.handler.netty.bytebuf.PooledBytebufFactory;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;

public final class ChannelUtil {
	/***
	 * 得到channel保存的ProtocolHeader数据
	 * @param channel
	 * @return
	 */
	public static IProtocolHeaderAdapter getProtolHeaderAdapter(Channel channel) {
		return channel.attr(ServerConstants.PROTOCOL_HEADER_ADAPTER).get();
	}

	/***
	 * 将一个MessageContent 转为 有 Header 的 ByteBuf
	 * @param content
	 * @param channel
	 * @return
	 */
	public static ByteBuf messageContentToByteBuf(MessageContent content, Channel channel) {
		IProtocolHeaderAdapter adapter = getProtolHeaderAdapter(channel);
		IProtocolHeader header = adapter.newHeader(content);

		ByteBuf byteBuf = PooledBytebufFactory.getInstance().alloc(header.getLength() + adapter.getHeaderLength());
		header.writeToByteBuf(byteBuf);
		byteBuf.writeBytes(content.bytes());
		return byteBuf;
	}
}
