package org.qiunet.flash.handler.context.response.push;

import java.nio.ByteBuffer;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/5/26 23:49
 **/
public class DefaultBytesMessage implements IChannelMessage<byte []> {

	private final int protocolId;

	private final byte [] message;

	public DefaultBytesMessage(int protocolId, byte[] message) {
		this.protocolId = protocolId;
		this.message = message;
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
		return message;
	}

	@Override
	public String toStr() {
		return "ProtocolID: "+protocolId;
	}

	@Override
	public ByteBuffer byteBuffer() {
		return ByteBuffer.wrap(this.message);
	}
}
