package org.qiunet.flash.handler.context.response.push;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.context.header.IProtocolHeaderType;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.utils.async.LazyLoader;

import java.nio.ByteBuffer;

/***
 * ByteBuf message的持有者父类
 *
 * @author qiunet
 * 2022/8/19 16:14
 */
public abstract class BaseByteBufMessage<T> implements IChannelMessage<T> {

	protected LazyLoader<ByteBuf> buffer = new LazyLoader<>(this::get);

	@Override
	public ByteBuf withHeaderByteBuf(Channel channel) {
		IProtocolHeaderType headerAdapter = ChannelUtil.getProtocolHeaderAdapter(channel);
		IProtocolHeader protocolHeader = headerAdapter.outHeader(this.getProtocolID(), this);
		ByteBuf byteBuf = Unpooled.wrappedBuffer(protocolHeader.headerByteBuf(), this.getByteBuf());
		protocolHeader.recycle();
		this.recycle();
		return byteBuf;
	}

	/**
	 * 是否已经有值
	 * @return
	 */
	public boolean isByteBufPrepare() {
		return buffer.isNotNull();
	}

	@Override
	public ByteBuffer byteBuffer() {
		return this.getByteBuf().nioBuffer();
	}


	public ByteBuf getByteBuf() {
		return buffer.get();
	}

	protected abstract ByteBuf get();
	@Override
	public ByteBuf withoutHeaderByteBuf() {
		return this.getByteBuf();
	}
}
