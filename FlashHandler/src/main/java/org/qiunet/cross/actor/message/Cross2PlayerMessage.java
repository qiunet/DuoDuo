package org.qiunet.cross.actor.message;

import io.netty.buffer.ByteBuf;
import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.response.push.BaseByteBufMessage;
import org.qiunet.utils.pool.ObjectPool;
import org.qiunet.utils.string.IDataToString;
import org.qiunet.utils.string.ToString;

/***
 *
 * 跨服转发客户端的消息
 *
 * @author qiunet
 * 2020-10-26 12:15
 */
public class Cross2PlayerMessage extends BaseByteBufMessage<IChannelData> implements IDataToString {
	private static final ObjectPool<Cross2PlayerMessage> RECYCLER = new ObjectPool<Cross2PlayerMessage>(1024, 32) {
		@Override
		public Cross2PlayerMessage newObject(Handle<Cross2PlayerMessage> handler) {
			return new Cross2PlayerMessage(handler);
		}
	};

	private final ObjectPool.Handle<Cross2PlayerMessage> recyclerHandle;

	/**
	 * 消息的协议id
	 */
	private int pid;

	private boolean flush;
	/**
	 * 是否走kcp通道
	 */
	private boolean kcpChannel;

	private boolean skipMessage;

	private IChannelData data;

	private Cross2PlayerMessage(ObjectPool.Handle<Cross2PlayerMessage> recyclerHandle) {
		this.recyclerHandle = recyclerHandle;
	}

	public static Cross2PlayerMessage valueOf(IChannelData responseData, boolean flush) {
		return valueOf(responseData, flush, false);
	}

	public static Cross2PlayerMessage valueOf(IChannelData responseData, boolean flush, boolean kcpChannel) {
		Cross2PlayerMessage response = RECYCLER.get();
		response.skipMessage = responseData.getClass().isAnnotationPresent(SkipDebugOut.class);
		response.pid = responseData.protocolId();
		response.kcpChannel = kcpChannel;
		response.data = responseData;
		response.flush = flush;
		return response;
	}

	@Override
	protected ByteBuf get() {
		return data.toByteBuf();
	}

	@Override
	public void recycle() {
		this.buffer.reset(true);
		this.skipMessage = false;
		this.kcpChannel = false;
		this.flush = false;
		this.data = null;
		this.pid = 0;
		recyclerHandle.recycle();
	}

	@Override
	public boolean needLogger() {
		if (this.skipMessage) {
			return false;
		}
		return super.needLogger();
	}

	public boolean isFlush() {
		return flush;
	}

	public boolean isKcpChannel() {
		return kcpChannel;
	}

	public int getPid() {
		return pid;
	}

	@Override
	public String _toString() {
		return ToString.toString(data);
	}

	@Override
	public IChannelData getContent() {
		return data;
	}

	@Override
	public int getProtocolID() {
		return pid;
	}
}
