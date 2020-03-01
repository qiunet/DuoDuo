package org.qiunet.flash.handler.context.response.push;

import org.qiunet.flash.handler.common.message.MessageContent;

import java.util.Arrays;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/5/26 23:49
 **/
public class DefaultBytesMessage implements IResponseMessage<byte []> {

	private int protocolId;
	private byte [] message;

	public DefaultBytesMessage(int protocolId, byte[] message) {
		this.protocolId = protocolId;
		this.message = message;
	}

	@Override
	public int getProtocolID() {
		return protocolId;
	}

	@Override
	public MessageContent encode() {
		return new MessageContent(protocolId, message);
	}

	@Override
	public byte[] getContent() {
		return message;
	}

	@Override
	public byte[] bytes() {
		return message;
	}

	@Override
	public String toStr() {
		StringBuilder sb = new StringBuilder();
		sb.append("===Response ProtocolID [").append(protocolId).append("] DATA: ");
		sb.append(Arrays.toString(message));
		return sb.toString();
	}
}
