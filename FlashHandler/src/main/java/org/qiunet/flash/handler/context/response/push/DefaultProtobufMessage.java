package org.qiunet.flash.handler.context.response.push;

import org.qiunet.cross.actor.message.Cross2PlayerResponse;
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
	/**
	 * 某种情况. 指定跳过debug信息
	 */
	private final boolean skipDebug;

	public DefaultProtobufMessage(int protocolId, IChannelData message) {
		skipDebug = message instanceof Cross2PlayerResponse && ((Cross2PlayerResponse) message).isSkipMessage();
		this.byteBuffer = message.toByteBuffer();
		this.protocolId = protocolId;
		this.message = message;
	}

	@Override
	public boolean needLogger() {
		if (skipDebug) {
			return false;
		}
		return IChannelMessage.super.needLogger();
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
