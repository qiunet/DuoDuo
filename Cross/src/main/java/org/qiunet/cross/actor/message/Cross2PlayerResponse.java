package org.qiunet.cross.actor.message;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.request.data.pb.IpbResponseData;
import org.qiunet.flash.handler.context.request.data.pb.PbResponse;

/***
 *
 *
 * @author qiunet
 * 2020-10-26 12:15
 */
@ProtobufClass(description = "跨服给客户端的包")
@PbResponse(IProtocolId.System.CROSS_2_PLAYER_MSG)
public class Cross2PlayerResponse implements IpbResponseData {
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

	public static Cross2PlayerResponse valueOf(IpbResponseData responseData) {
		Cross2PlayerResponse response = new Cross2PlayerResponse();
		response.pid = responseData.getProtocolId();
		response.bytes = responseData.toByteArray();
		return response;
	}

	public MessageContent buildMessageContent(){
		return new MessageContent(pid, bytes);
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
}
