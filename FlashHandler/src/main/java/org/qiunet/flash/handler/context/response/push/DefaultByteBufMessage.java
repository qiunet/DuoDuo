package org.qiunet.flash.handler.context.response.push;

import io.netty.buffer.ByteBuf;
import org.qiunet.utils.pool.ObjectPool;

/***
 *
 * @Author qiunet
 * @Date Create in 2022/6/13 09:24
 **/
public class DefaultByteBufMessage extends BaseByteBufMessage<ByteBuf> {
	private static final ObjectPool<DefaultByteBufMessage> RECYCLER = new ObjectPool<DefaultByteBufMessage>(1024, 32) {
		@Override
		public DefaultByteBufMessage newObject(Handle<DefaultByteBufMessage> handler) {
			return new DefaultByteBufMessage(handler);
		}
	};

	private final ObjectPool.Handle<DefaultByteBufMessage> recyclerHandle;

	private ByteBuf byteBuf;

	private int protocolId;

	public DefaultByteBufMessage(ObjectPool.Handle<DefaultByteBufMessage> recyclerHandle) {
		this.recyclerHandle = recyclerHandle;
	}

	public static DefaultByteBufMessage valueOf(int protocolId, ByteBuf buffer) {
		DefaultByteBufMessage data = RECYCLER.get();
		data.protocolId = protocolId;
		data.byteBuf = buffer;
		return data;
	}

	@Override
	public boolean isByteBufPrepare() {
		return true;
	}

	@Override
	protected ByteBuf get() {
		return this.byteBuf;
	}

	@Override
	public void recycle() {
		this.buffer.reset(true);
		this.protocolId = 0;
		this.byteBuf = null;
		this.recyclerHandle.recycle();
	}

	@Override
	public boolean needLogger() {
		return false;
	}

	@Override
	public int getProtocolID() {
		return protocolId;
	}

	@Override
	public ByteBuf getContent() {
		return byteBuf;
	}

	@Override
	public String toStr() {
		return "ProtocolID: "+protocolId;
	}
}
