package org.qiunet.flash.handler.netty.server.param.adapter.message;

import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

/***
 *
 * 服务器响应客户端ping信息
 * qiunet
 * 2021/9/23 17:17
 **/
@ChannelData(ID = IProtocolId.System.SERVER_PONG, desc = "服务器pong信息")
public class ServerPongResponse implements IChannelData {
	@Ignore
	private static final ServerPongResponse instance = new ServerPongResponse();

	@Protobuf(description = "客户端自定义数据. 服务器会在pong返回")
	private byte[] bytes;

	public ServerPongResponse() {}

	public ServerPongResponse(byte[] bytes) {
		this.bytes = bytes;
	}

	public static ServerPongResponse valueOf(byte[] bytes){
		if (bytes == null || bytes.length == 0) {
			return instance;
		}
		return new ServerPongResponse(bytes);
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
}
