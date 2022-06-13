package org.qiunet.flash.handler.common.message;

import org.qiunet.flash.handler.context.header.IProtocolHeader;

import java.nio.ByteBuffer;

/**
 *  上下行消息的封装类.
 *  MessageContent 只跟bytebuffer 交道.
 *
 * @author qiunet
 *         Created on 17/3/13 19:50.
 */
public class MessageContent {
	/**
	 * 数据内容
	 */
	private final ByteBuffer buffer;
	/**
	 * 协议ID
	 */
	private final int protocolId;
	/**
	 * 请求的URI
	 */
	private final String uriPath;
	/**
	 * 头信息
	 */
	private final IProtocolHeader header;

	public MessageContent(String uriPath, ByteBuffer buffer) {
		this.uriPath = uriPath;
		this.buffer = buffer;
		this.header = null;
		this.protocolId = 0;
	}

	public MessageContent(IProtocolHeader header, ByteBuffer buffer) {
		this.protocolId = header.getProtocolId();
		this.buffer = buffer;
		this.uriPath = null;
		this.header = header;
	}

	public String getUriPath() {
		return uriPath;
	}

	public int getProtocolId() {
		return protocolId;
	}

	public IProtocolHeader getHeader() {
		return header;
	}

	public ByteBuffer byteBuffer(){
		return this.buffer;
	}

	@Override
	public String toString() {
		if (protocolId > 0) {
			return "protocolID: " + protocolId;
		}
		return "uriPath: "+uriPath;
	}
}
