package org.qiunet.flash.handler.context.response.push;

import io.netty.util.CharsetUtil;

/**
 * 默认的string 的message
 * Created by qiunet.
 * 17/12/11
 */
public class DefaultStringMessage implements IChannelMessage<String> {

	private final int protocolId;

	private final String message;

	private final byte[] bytes;
	public DefaultStringMessage(int protocolId, String message) {
		this.message = message;
		this.protocolId = protocolId;
		this.bytes = message.getBytes(CharsetUtil.UTF_8);
	}

	@Override
	public byte[] bytes() {
		return bytes;
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
