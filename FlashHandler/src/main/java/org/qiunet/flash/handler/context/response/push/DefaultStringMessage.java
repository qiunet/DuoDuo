package org.qiunet.flash.handler.context.response.push;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * 默认的string 的message
 * Created by qiunet.
 * 17/12/11
 */
public class DefaultStringMessage implements IChannelMessage<String> {

	private final int protocolId;

	private final String message;

	private final ByteBuffer buffer;

	public DefaultStringMessage(int protocolId, String message) {
		this.buffer = StandardCharsets.UTF_8.encode(message);
		this.protocolId = protocolId;
		this.message = message;
	}

	@Override
	public ByteBuffer byteBuffer() {
		return buffer;
	}

	@Override
	public String getContent() {
		return message;
	}

	@Override
	public int getProtocolID() {
		return protocolId;
	}
}
