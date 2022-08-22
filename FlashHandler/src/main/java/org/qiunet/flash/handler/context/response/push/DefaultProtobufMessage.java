package org.qiunet.flash.handler.context.response.push;

import io.netty.buffer.ByteBuf;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.utils.pool.ObjectPool;

/**
 * 默认的protobuf 的message
 * Created by qiunet.
 * 17/12/11
 */
public class DefaultProtobufMessage extends BaseByteBufMessage<IChannelData> {
	private static final ObjectPool<DefaultProtobufMessage> RECYCLER = new ObjectPool<DefaultProtobufMessage>() {
		@Override
		public DefaultProtobufMessage newObject(Handle<DefaultProtobufMessage> handler) {
			return new DefaultProtobufMessage(handler);
		}
	};

	private final ObjectPool.Handle<DefaultProtobufMessage> recyclerHandle;
	/**
	 * 消息id
	 */
	private int protocolId;
	/**
	 * 消息体
	 */
	private IChannelData message;

	public DefaultProtobufMessage(ObjectPool.Handle<DefaultProtobufMessage> recyclerHandle) {
		this.recyclerHandle = recyclerHandle;
	}

	public static DefaultProtobufMessage valueOf(int protocolId, IChannelData message) {
		DefaultProtobufMessage data = RECYCLER.get();
		data.protocolId = protocolId;
		data.message = message;
		return data;
	}

	@Override
	protected ByteBuf get() {
		return message.toByteBuf();
	}

	@Override
	public void recycle() {
		this.buffer.reset(true);
		this.protocolId = 0;
		this.message = null;
		recyclerHandle.recycle();
	}

	@Override
	public IChannelData getContent() {
		return message;
	}


	@Override
	public int getProtocolID() {
		return protocolId;
	}
}
