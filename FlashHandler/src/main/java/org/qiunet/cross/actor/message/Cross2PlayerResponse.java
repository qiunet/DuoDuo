package org.qiunet.cross.actor.message;

import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.util.proto.SkipProtoGenerator;
import org.qiunet.utils.string.IDataToString;
import org.qiunet.utils.string.ToString;

/***
 *
 *
 * @author qiunet
 * 2020-10-26 12:15
 */
@SkipProtoGenerator
@ChannelData(ID = IProtocolId.System.CROSS_2_PLAYER_MSG, desc = "跨服给客户端的包")
public class Cross2PlayerResponse implements IChannelData, IDataToString {
	/**
	 * 消息的协议id
	 */
	@Protobuf(description = "协议id")
	private int pid;
	/**
	 * 消息的内容.
	 */
	@Protobuf(description = "消息的内容")
	private byte [] bytes;
	/**
	 * 是否走kcp通道
	 */
	@Protobuf(description = "走kcp通道")
	private boolean kcpChannel;

	@Protobuf(description = "是否flush")
	private boolean flush;
	@Ignore
	private IChannelData data;
	@Ignore
	private boolean skipMessage;

	public static Cross2PlayerResponse valueOf(IChannelData responseData, boolean flush) {
		return valueOf(responseData, flush, false);
	}

	public static Cross2PlayerResponse valueOf(IChannelData responseData, boolean flush, boolean kcpChannel) {
		Cross2PlayerResponse response = new Cross2PlayerResponse();
		response.skipMessage = responseData.getClass().isAnnotationPresent(SkipDebugOut.class);
		response.bytes = responseData.toByteBuffer().array();
		response.pid = responseData.protocolId();
		response.kcpChannel = kcpChannel;
		response.data = responseData;
		response.flush = flush;
		return response;
	}

	public boolean isSkipMessage() {
		return skipMessage;
	}

	public boolean isFlush() {
		return flush;
	}

	public void setFlush(boolean flush) {
		this.flush = flush;
	}

	public boolean isKcpChannel() {
		return kcpChannel;
	}

	public void setKcpChannel(boolean kcpChannel) {
		this.kcpChannel = kcpChannel;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	@Override
	public String _toString() {
		return ToString.toString(data);
	}
}
