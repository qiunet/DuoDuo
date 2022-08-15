package org.qiunet.flash.handler.context.response.push;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.context.header.IProtocolHeaderType;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.utils.pool.ObjectPool;

import java.nio.ByteBuffer;

/***
 *
 * @Author qiunet
 * @Date Create in 2022/6/13 09:24
 **/
public class DefaultByteBufMessage implements IChannelMessage<ByteBuf> {
	private static final ObjectPool<DefaultByteBufMessage> RECYCLER = new ObjectPool<DefaultByteBufMessage>(1024, 32) {
		@Override
		public DefaultByteBufMessage newObject(Handle<DefaultByteBufMessage> handler) {
			return new DefaultByteBufMessage(handler);
		}
	};

	private final ObjectPool.Handle<DefaultByteBufMessage> recyclerHandle;
	private int protocolId;

	private ByteBuf buffer;

	public DefaultByteBufMessage(ObjectPool.Handle<DefaultByteBufMessage> recyclerHandle) {
		this.recyclerHandle = recyclerHandle;
	}

	public static DefaultByteBufMessage valueOf(int protocolId, ByteBuf buffer) {
		DefaultByteBufMessage data = RECYCLER.get();
		data.protocolId = protocolId;
		data.buffer = buffer;
		return data;
	}

	@Override
	public void recycle() {
		this.protocolId = 0;
		this.buffer = null;
		this.recyclerHandle.recycle();
	}

	@Override
	public boolean needLogger() {
		return false;
	}

	@Override
	public int getProtocolID() {
		return protocolId;
	}

	@Override
	public ByteBuf getContent() {
		return buffer;
	}

	@Override
	public String toStr() {
		return "ProtocolID: "+protocolId;
	}

	@Override
	public ByteBuffer byteBuffer() {
		return buffer.nioBuffer();
	}

	@Override
	public ByteBuf withHeaderByteBuf(Channel channel) {
		IProtocolHeaderType headerAdapter = ChannelUtil.getProtocolHeaderAdapter(channel);
		IProtocolHeader protocolHeader = headerAdapter.outHeader(this.getProtocolID(), this);
		ByteBuf byteBuf = Unpooled.wrappedBuffer(Unpooled.wrappedBuffer(((ByteBuffer) protocolHeader.dataBytes().rewind())), this.buffer);
		protocolHeader.recycle();
		this.recycle();
		return byteBuf;
	}

	@Override
	public ByteBuf withoutHeaderByteBuf() {
		return this.buffer;
	}
}
