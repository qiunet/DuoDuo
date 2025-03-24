package org.qiunet.flash.handler.common.message;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import org.qiunet.flash.handler.common.protobuf.ProtoDecodeException;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.utils.data.ByteUtil;
import org.qiunet.utils.pool.ObjectPool;

import java.nio.ByteBuffer;

/**
 *  上下行消息的封装类.
 *  MessageContent 只跟bytebuffer 交道.
 *
 * @author qiunet
 *         Created on 17/3/13 19:50.
 */
public class MessageContent implements ReferenceCounted {
	private static final org.qiunet.utils.pool.ObjectPool<MessageContent> RECYCLER = new ObjectPool<MessageContent>() {
		@Override
		public MessageContent newObject(Handle<MessageContent> handler) {
			return new MessageContent(handler);
		}
	};

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
	private IProtocolHeader.ProtocolHeader header;

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

	public static MessageContent valueOf(IProtocolHeader.ProtocolHeader header, ByteBuf buffer) {
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
		this.recyclerHandle.recycle();
	}

	public String getUriPath() {
		return uriPath;
	}

	public int getProtocolId() {
		return protocolId;
	}

	public IProtocolHeader.ProtocolHeader getHeader() {
		return header;
	}

	public ByteBuffer byteBuffer(){
		return this.buffer.nioBuffer();
	}

	public ByteBuf byteBuf() {
		return this.buffer;
	}

	/**
	 * 解析protobuf
	 * @param clz class
	 * @return channel Data
	 * @param <T> IChannelData 类型
	 */
	public <T extends IChannelData> T decodeProtobuf(Class<T> clz) {
        try {
            return ProtobufDataManager.decode(clz, this.byteBuffer());
        } catch (Exception e) {
            throw new ProtoDecodeException(e, clz, this.header, ByteUtil.readBytebuffer(this.byteBuffer()));
        }
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

	public <T extends IChannelData> T decode(Class<T> clz) {
		try {
			return ProtobufDataManager.decode(clz, this.byteBuffer());
		}catch (Throwable e) {
			throw new ProtoDecodeException(e, clz, header, ByteUtil.readBytebuffer(this.byteBuffer()));
		}
	}
}
