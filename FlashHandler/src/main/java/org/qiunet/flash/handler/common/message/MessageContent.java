package org.qiunet.flash.handler.common.message;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import io.netty.util.internal.ObjectPool;
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
	private static final ObjectPool<MessageContent> RECYCLER = ObjectPool.newPool(MessageContent::new);
	private final ObjectPool.Handle<MessageContent> recyclerHandle;
	/**
	 * 数据内容
	 */
	private ByteBuf buffer;
	/**
	 * 协议ID
	 */
	private int protocolId;
	/**
	 * 请求的URI
	 */
	private String uriPath;
	/**
	 * 头信息
	 */
	private IProtocolHeader header;

	public MessageContent(ObjectPool.Handle<MessageContent> recyclerHandle) {
		this.recyclerHandle = recyclerHandle;
	}

	public static MessageContent valueOf(String uriPath, ByteBuf buffer) {
		MessageContent content = RECYCLER.get();
		content.uriPath = uriPath;
		content.buffer = buffer;
		content.header = null;
		content.protocolId = 0;
		return content;
	}

	public static MessageContent valueOf(IProtocolHeader header, ByteBuf buffer) {
		MessageContent content = RECYCLER.get();
		content.protocolId = header.getProtocolId();
		content.buffer = buffer;
		content.uriPath = null;
		content.header = header;
		return content;
	}

	public void recycle() {
		this.protocolId = 0;
		this.buffer = null;
		this.uriPath = null;
		if (this.header != null) {
			this.header.recycle();
		}
		this.header = null;
		this.recyclerHandle.recycle(this);
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
		return this.release(1);
	}

	@Override
	public boolean release(int decrement) {
		if (ReferenceCountUtil.release(content(), decrement)) {
			this.recycle();
			return true;
		}
		return false;
	}
}
