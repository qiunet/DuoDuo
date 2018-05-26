package org.qiunet.flash.handler.context.response.push;

import com.google.protobuf.GeneratedMessageV3;
import com.googlecode.protobuf.format.JsonFormat;
import org.qiunet.flash.handler.common.message.MessageContent;

/**
 * 默认的protobuf 的message
 * Created by qiunet.
 * 17/12/11
 */
public class DefaultProtobufMessage implements IMessage<GeneratedMessageV3> {
	private static final JsonFormat jsonFormat = new JsonFormat();

	private int protocolId;
	private GeneratedMessageV3 message;

	public DefaultProtobufMessage(int protocolId, GeneratedMessageV3 message) {
		this.message = message;
		this.protocolId = protocolId;
	}

	@Override
	public MessageContent encode() {
		return new MessageContent(protocolId, message.toByteArray());
	}

	@Override
	public GeneratedMessageV3 getContent() {
		return message;
	}

	@Override
	public int getProtocolID() {
		return protocolId;
	}

	@Override
	public String toStr() {
		StringBuilder sb = new StringBuilder();
		sb.append("===Response ProtocolID [").append(protocolId).append("] ==DATA: ");
		sb.append(jsonFormat.printToString(message));
		return sb.toString();
	}
}
