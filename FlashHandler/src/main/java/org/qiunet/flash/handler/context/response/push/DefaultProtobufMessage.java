package org.qiunet.flash.handler.context.response.push;

import org.qiunet.flash.handler.context.request.data.IChannelData;

import java.nio.ByteBuffer;

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
	private final ByteBuffer byteBuffer;

	public DefaultProtobufMessage(int protocolId, IChannelData message) {
		this.byteBuffer = message.toByteBuffer();
		this.protocolId = protocolId;
		this.message = message;
	}

	@Override
	public IChannelData getContent() {
		return message;
	}

	@Override
	public ByteBuffer byteBuffer() {
		return this.byteBuffer;
	}

	@Override
	public int getProtocolID() {
		return protocolId;
	}
}
