package org.qiunet.flash.handler.context.response.push;

import java.nio.ByteBuffer;

/***
 *
 * @Author qiunet
 * @Date Create in 2022/6/13 09:24
 **/
public class DefaultByteBufferMessage implements IChannelMessage<ByteBuffer> {

	private final int protocolId;

	private final ByteBuffer buffer;

	public DefaultByteBufferMessage(int protocolId, ByteBuffer buffer) {
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
	public ByteBuffer getContent() {
		return buffer;
	}

	@Override
	public String toStr() {
		return "ProtocolID: "+protocolId;
	}

	@Override
	public ByteBuffer byteBuffer() {
		return buffer;
	}
}
