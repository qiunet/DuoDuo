package org.qiunet.flash.handler.context.response.push;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.context.header.IProtocolHeaderType;
import org.qiunet.flash.handler.util.ChannelUtil;

import java.nio.ByteBuffer;

/***
 *
 * @author qiunet
 * 2022/8/19 16:14
 */
public abstract class BaseByteBufMessage<T> implements IChannelMessage<T> {

	protected ByteBuf buffer;

	@Override
	public ByteBuf withHeaderByteBuf(Channel channel) {
		IProtocolHeaderType headerAdapter = ChannelUtil.getProtocolHeaderAdapter(channel);
		IProtocolHeader protocolHeader = headerAdapter.outHeader(this.getProtocolID(), this);
		ByteBuf byteBuf = Unpooled.wrappedBuffer(Unpooled.wrappedBuffer(((ByteBuffer) protocolHeader.dataBytes().rewind())), this.buffer);
		protocolHeader.recycle();
		this.recycle();
		return byteBuf;
	}

	public ByteBuf getByteBuf() {
		return buffer;
	}

	@Override
	public ByteBuf withoutHeaderByteBuf() {
		return this.buffer;
	}
}
