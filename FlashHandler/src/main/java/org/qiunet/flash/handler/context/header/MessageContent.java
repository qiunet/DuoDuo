package org.qiunet.flash.handler.context.header;

import org.qiunet.flash.handler.context.request.attribute.IAttributeData;

import java.util.HashMap;
import java.util.Map;

/**
 *  上下行消息的封装类.
 *  netty 只跟byte数组打交道.
 *  其它自行解析
 * @author qiunet
 *         Created on 17/3/13 19:50.
 */
public class MessageContent implements IAttributeData {
	protected byte [] bytes;
	protected int protocolId;
	private Map<String, Object> attributes;

	private String uriPath;

	public MessageContent(int protocolId, byte [] bytes) {
		this.bytes = bytes;
		this.protocolId = protocolId;
		this.attributes = new HashMap<>();
	}

	public MessageContent(String uriPath, byte [] bytes) {
		this.bytes = bytes;
		this.uriPath = uriPath;
		this.attributes = new HashMap<>();
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

	@Override
	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	@Override
	public void setAttribute(String key, Object val) {
		attributes.put(key, val);
	}
}
