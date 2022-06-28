package org.qiunet.flash.handler.context.response.push;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCounted;

import java.nio.ByteBuffer;

/***
 *
 * @Author qiunet
 * @Date Create in 2022/6/13 09:24
 **/
public class DefaultByteBufMessage implements IChannelMessage<ByteBuf>, ReferenceCounted {

	private final int protocolId;

	private final ByteBuf buffer;

	public DefaultByteBufMessage(int protocolId, ByteBuf buffer) {
		this.protocolId = protocolId;
		this.buffer = buffer;
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
	public int refCnt() {
		return this.buffer.refCnt();
	}

	@Override
	public ReferenceCounted retain() {
		return this.buffer.retain();
	}

	@Override
	public ReferenceCounted retain(int increment) {
		return this.buffer.retain(increment);
	}

	@Override
	public ReferenceCounted touch() {
		return this.buffer.touch();
	}

	@Override
	public ReferenceCounted touch(Object hint) {
		return this.buffer.touch(hint);
	}

	@Override
	public boolean release() {
		return this.buffer.release();
	}

	@Override
	public boolean release(int decrement) {
		return this.buffer.release(decrement);
	}
}
