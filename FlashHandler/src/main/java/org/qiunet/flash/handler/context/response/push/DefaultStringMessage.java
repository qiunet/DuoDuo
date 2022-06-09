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

	public DefaultStringMessage(int protocolId, String message) {
		this.message = message;
		this.protocolId = protocolId;
	}

	@Override
	public ByteBuffer byteBuffer() {
		return StandardCharsets.UTF_8.encode(this.message);
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
