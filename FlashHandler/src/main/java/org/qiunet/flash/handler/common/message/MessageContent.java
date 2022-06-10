package org.qiunet.flash.handler.common.message;

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

	public MessageContent(String uriPath, ByteBuffer buffer) {
		this.uriPath = uriPath;
		this.buffer = buffer;
		this.protocolId = 0;
	}

	public MessageContent(int protocolID, ByteBuffer buffer) {
		this.protocolId = protocolID;
		this.buffer = buffer;
		this.uriPath = null;
	}

	public String getUriPath() {
		return uriPath;
	}

	public int getProtocolId() {
		return protocolId;
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
