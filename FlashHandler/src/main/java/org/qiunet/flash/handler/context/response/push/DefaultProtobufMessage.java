package org.qiunet.flash.handler.context.response.push;

import com.google.protobuf.GeneratedMessageV3;
import org.qiunet.flash.handler.common.message.MessageContent;

/**
 * 默认的protobuf 的message
 * Created by qiunet.
 * 17/12/11
 */
public class DefaultProtobufMessage implements IResponseMessage<GeneratedMessageV3> {

	private int protocolId;
	private GeneratedMessageV3 message;

	public DefaultProtobufMessage(int protocolId, GeneratedMessageV3 message) {
		this.message = message;
		this.protocolId = protocolId;
	}

	@Override
	public MessageContent encode() {
		return new MessageContent(protocolId, bytes());
	}

	@Override
	public GeneratedMessageV3 getContent() {
		return message;
	}

	@Override
	public byte[] bytes() {
		return message.toByteArray();
	}

	@Override
	public int getProtocolID() {
		return protocolId;
	}

	@Override
	public String toStr() {
		StringBuilder sb = new StringBuilder();
		sb.append("===Response ProtocolID [").append(protocolId).append("] ==DATA: ");
		return sb.toString();
	}
}
