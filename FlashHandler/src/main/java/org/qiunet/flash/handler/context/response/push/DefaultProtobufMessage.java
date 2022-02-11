package org.qiunet.flash.handler.context.response.push;

import org.qiunet.flash.handler.context.request.data.IChannelData;

/**
 * 默认的protobuf 的message
 * Created by qiunet.
 * 17/12/11
 */
public class DefaultProtobufMessage implements IChannelMessage<IChannelData> {
	/**
	 * 消息id
	 */
	private final int protocolId;
	/**
	 * 消息体
	 */
	private final IChannelData message;
	/**
	 * 消息体内容
	 */
	private final byte [] bytes;
	public DefaultProtobufMessage(int protocolId, IChannelData message) {
		this.message = message;
		this.protocolId = protocolId;
		this.bytes = message.toByteArray();
	}

	@Override
	public IChannelData getContent() {
		return message;
	}

	@Override
	public byte[] bytes() {
		return bytes;
	}

	@Override
	public int getProtocolID() {
		return protocolId;
	}
}
