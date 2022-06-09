package org.qiunet.flash.handler.common.message;

import io.netty.buffer.ByteBuf;
import org.qiunet.utils.string.StringUtil;

import java.nio.ByteBuffer;

/**
 *  上下行消息的封装类.
 *  netty 只跟byte数组打交道.
 *  其它自行解析
 * @author qiunet
 *         Created on 17/3/13 19:50.
 */
public class MessageContent {
	private final ByteBuffer buffer;
	private final int protocolId;
	private final String uriPath;

	public MessageContent(int protocolId, ByteBuf buffer) {
		this.buffer = buffer.nioBuffer();
		this.protocolId = protocolId;
		this.uriPath = null;
		buffer.release();
	}

	public MessageContent(String uriPath, ByteBuf buffer) {
		this.buffer = buffer.nioBuffer();
		this.uriPath = uriPath;
		this.protocolId = 0;
		buffer.release();
	}

	public MessageContent(int protocolID, ByteBuffer buffer) {
		this.protocolId = protocolID;
		this.buffer = buffer;
		this.uriPath = null;
	}

	public boolean isUriPathMsg(){
		return !StringUtil.isEmpty(uriPath);
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
