package org.qiunet.flash.handler.context.response.push;

import java.nio.ByteBuffer;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/5/26 23:49
 **/
public class DefaultBytesMessage implements IChannelMessage<byte []> {

	private final int protocolId;

	private final ByteBuffer buffer;

	public DefaultBytesMessage(int protocolId, byte[] message) {
		this.buffer = ByteBuffer.wrap(message);
		this.protocolId = protocolId;
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
	public byte[] getContent() {
		return buffer.array();
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
