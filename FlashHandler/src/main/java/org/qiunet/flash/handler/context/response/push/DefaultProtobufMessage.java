package org.qiunet.flash.handler.context.response.push;

import io.netty.util.internal.ObjectPool;
import org.qiunet.flash.handler.context.request.data.IChannelData;

import java.nio.ByteBuffer;

/**
 * 默认的protobuf 的message
 * Created by qiunet.
 * 17/12/11
 */
public class DefaultProtobufMessage implements IChannelMessage<IChannelData> {
	private static final ObjectPool<DefaultProtobufMessage> RECYCLER = ObjectPool.newPool(DefaultProtobufMessage::new);
	private final ObjectPool.Handle<DefaultProtobufMessage> recyclerHandle;
	/**
	 * 消息id
	 */
	private int protocolId;
	/**
	 * 消息体
	 */
	private IChannelData message;
	/**
	 * 消息体内容
	 */
	private ByteBuffer byteBuffer;

	public DefaultProtobufMessage(ObjectPool.Handle<DefaultProtobufMessage> recyclerHandle) {
		this.recyclerHandle = recyclerHandle;
	}

	public static DefaultProtobufMessage valueOf(int protocolId, IChannelData message) {
		DefaultProtobufMessage data = RECYCLER.get();
		data.byteBuffer = message.toByteBuffer();
		data.protocolId = protocolId;
		data.message = message;
		return data;
	}

	@Override
	public void recycle() {
		this.byteBuffer = null;
		this.protocolId = 0;
		this.message = null;
		recyclerHandle.recycle(this);
	}

	@Override
	public IChannelData getContent() {
		return message;
	}

	@Override
	public ByteBuffer byteBuffer() {
		return this.byteBuffer;
	}

	@Override
	public int getProtocolID() {
		return protocolId;
	}
}
