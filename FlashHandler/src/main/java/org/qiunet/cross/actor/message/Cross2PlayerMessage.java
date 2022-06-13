package org.qiunet.cross.actor.message;

import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.utils.string.IDataToString;
import org.qiunet.utils.string.ToString;

import java.nio.ByteBuffer;

/***
 *
 * 跨服转发客户端的消息
 *
 * @author qiunet
 * 2020-10-26 12:15
 */
public class Cross2PlayerMessage implements IChannelMessage<IChannelData>, IDataToString {
	/**
	 * 消息的协议id
	 */
	private int pid;

	private boolean flush;
	/**
	 * 消息的内容.
	 */
	private ByteBuffer buffer;
	/**
	 * 是否走kcp通道
	 */
	private boolean kcpChannel;

	private boolean skipMessage;

	private IChannelData data;


	public static Cross2PlayerMessage valueOf(IChannelData responseData, boolean flush) {
		return valueOf(responseData, flush, false);
	}

	public static Cross2PlayerMessage valueOf(IChannelData responseData, boolean flush, boolean kcpChannel) {
		Cross2PlayerMessage response = new Cross2PlayerMessage();
		response.skipMessage = responseData.getClass().isAnnotationPresent(SkipDebugOut.class);
		response.buffer = responseData.toByteBuffer();
		response.pid = responseData.protocolId();
		response.kcpChannel = kcpChannel;
		response.data = responseData;
		response.flush = flush;
		return response;
	}

	@Override
	public boolean needLogger() {
		if (this.skipMessage) {
			return false;
		}
		return IChannelMessage.super.needLogger();
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
	public ByteBuffer byteBuffer() {
		return buffer;
	}

	@Override
	public int getProtocolID() {
		return pid;
	}
}
