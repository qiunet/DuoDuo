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
	private final ByteBuf buffer;
	private byte [] bytes;
	private int protocolId;
	private String uriPath;

	public MessageContent(int protocolId, ByteBuf buffer) {
		this.buffer = buffer;
		this.protocolId = protocolId;
	}

	public MessageContent(String uriPath, ByteBuf buffer) {
		this.uriPath = uriPath;
		this.buffer = buffer;
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
		if (bytes != null) {
			return ByteBuffer.wrap(bytes);
		}

		return this.buffer.nioBuffer();
	}

	/***
	 * 如果自己处理ByteBuf 需要release.
	 * @return
	 */
	public ByteBuf byteBuf() {
		return buffer;
	}

	public void release(){
		buffer.release();
	}

	public boolean hasArray(){
		return bytes != null;
	}

	/**
	 * 获得数据,
	 * 读取后, 会释放掉 bytebuf
	 * @return
	 */
	public byte[] bytes(){
		if (this.hasArray()) {
			return bytes;
		}

		return getBufferBytes();
	}

	/**
	 * 从buffer中获取bytes
	 * @return
	 */
	private synchronized byte[] getBufferBytes(){
		if (hasArray()) {
			return bytes;
		}

		try {
			this.bytes = new byte[buffer.readableBytes()];
			buffer.readBytes(bytes);
			return bytes;
		}finally {
			buffer.release();
		}
	}

	@Override
	public String toString() {
		if (protocolId > 0) {
			return "protocolID: " + protocolId;
		}
		return "uriPath: "+uriPath;
	}
}
