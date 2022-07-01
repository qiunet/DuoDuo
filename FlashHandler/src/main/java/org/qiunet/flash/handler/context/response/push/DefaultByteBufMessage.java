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
 * @Author qiunet
 * @Date Create in 2022/6/13 09:24
 **/
public class DefaultByteBufMessage implements IChannelMessage<ByteBuf> {

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
	public ByteBuf withHeaderByteBuf(Channel channel) {
		IProtocolHeaderType headerAdapter = ChannelUtil.getProtocolHeaderAdapter(channel);
		IProtocolHeader protocolHeader = headerAdapter.outHeader(this.getProtocolID(), this);
		return Unpooled.wrappedBuffer(Unpooled.wrappedBuffer(((ByteBuffer) protocolHeader.dataBytes().rewind())), this.buffer);
	}

	@Override
	public ByteBuf withoutHeaderByteBuf() {
		return this.buffer;
	}
}
