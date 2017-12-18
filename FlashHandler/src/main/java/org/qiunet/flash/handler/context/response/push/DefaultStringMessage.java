package org.qiunet.flash.handler.context.response.push;

import io.netty.util.CharsetUtil;
import org.qiunet.flash.handler.common.message.MessageContent;

/**
 * 默认的string 的message
 * Created by qiunet.
 * 17/12/11
 */
public class DefaultStringMessage implements IMessage {

	private int protocolId;
	private String message;

	public DefaultStringMessage(int protocolId, String message) {
		this.message = message;
		this.protocolId = protocolId;
	}

	@Override
	public MessageContent encode() {
		return new MessageContent(protocolId, message.getBytes(CharsetUtil.UTF_8));
	}
}