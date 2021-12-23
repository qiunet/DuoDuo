package org.qiunet.flash.handler.netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.util.ChannelUtil;

/**
 * Created by qiunet.
 * 17/8/13
 */
public class TcpSocketEncoder extends MessageToByteEncoder<IChannelMessage<?>> {
	@Override
	protected void encode(ChannelHandlerContext ctx, IChannelMessage<?> msg, ByteBuf out) throws Exception {
		ChannelUtil.messageContentToByteBuf(msg.encode(), ctx.channel(), out);
	}
}
