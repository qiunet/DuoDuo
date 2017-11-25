package org.qiunet.flash.handler.context.header;

import java.util.Map;

/**
 *  上下行消息的封装类.
 *  netty 只跟byte数组打交道.
 *  其它自行解析
 * @author qiunet
 *         Created on 17/3/13 19:50.
 */
public class MessageContent {
	protected byte [] bytes;
	protected int protocolId;
	private Map<String, Object> attributes;

	private String uriPath;

	public MessageContent(int protocolId, byte [] bytes) {
		this.bytes = bytes;
		this.protocolId = protocolId;
	}

	public MessageContent(String uriPath, byte [] bytes) {
		this.bytes = bytes;
		this.uriPath = uriPath;
	}

	public int getProtocolId() {
		return protocolId;
	}

	public byte [] bytes() {
		return bytes;
	}

	public String getUriPath() {
		return uriPath;
	}
}
