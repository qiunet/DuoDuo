package org.qiunet.flash.handler.common.message;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import org.qiunet.flash.handler.context.header.IProtocolHeader;

import java.nio.ByteBuffer;

/**
 *  上下行消息的封装类.
 *  MessageContent 只跟bytebuffer 交道.
 *
 * @author qiunet
 *         Created on 17/3/13 19:50.
 */
public class MessageContent implements ReferenceCounted {
	/**
	 * 数据内容
	 */
	private final ByteBuf buffer;
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

	public MessageContent(String uriPath, ByteBuf buffer) {
		this.uriPath = uriPath;
		this.buffer = buffer;
		this.header = null;
		this.protocolId = 0;
	}

	public MessageContent(IProtocolHeader header, ByteBuf buffer) {
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
		return this.buffer.nioBuffer();
	}

	public ByteBuf byteBuf() {
		return this.buffer;
	}

	@Override
	public String toString() {
		if (protocolId > 0) {
			return "protocolID: " + protocolId;
		}
		return "uriPath: "+uriPath;
	}

	@Override
	public int refCnt() {
		return buffer.refCnt();
	}

	@Override
	public MessageContent retain() {
		ReferenceCountUtil.retain(buffer);
		return this;
	}

	@Override
	public MessageContent retain(int increment) {
		ReferenceCountUtil.retain(content(), increment);
		return this;
	}

	private ByteBuf content() {
		return this.buffer;
	}

	@Override
	public MessageContent touch() {
		ReferenceCountUtil.touch(content());
		return this;
	}

	@Override
	public MessageContent touch(Object hint) {
		ReferenceCountUtil.touch(content(), hint);
		return this;
	}

	@Override
	public boolean release() {
		return ReferenceCountUtil.release(content());
	}

	@Override
	public boolean release(int decrement) {
		return ReferenceCountUtil.release(content(), decrement);
	}
}
