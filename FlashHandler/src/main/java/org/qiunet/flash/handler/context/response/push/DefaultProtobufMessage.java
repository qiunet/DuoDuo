package org.qiunet.flash.handler.context.response.push;

import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.request.data.pb.IpbResponseData;

/**
 * 默认的protobuf 的message
 * Created by qiunet.
 * 17/12/11
 */
public class DefaultProtobufMessage implements IResponseMessage<IpbResponseData> {

	private int protocolId;
	private IpbResponseData message;

	public DefaultProtobufMessage(int protocolId, IpbResponseData message) {
		this.message = message;
		this.protocolId = protocolId;
	}

	@Override
	public MessageContent encode() {
		return new MessageContent(protocolId, bytes());
	}

	@Override
	public IpbResponseData getContent() {
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
}
