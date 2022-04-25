package org.qiunet.flash.handler.netty.server.param.adapter.message;

import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

/***
 *
 * 请求ping信息
 * qiunet
 * 2021/9/23 17:17
 **/
@SkipDebugOut
@ChannelData(ID = IProtocolId.System.CLIENT_PING, desc = "ping信息")
public class ClientPingRequest implements IChannelData {
	@Ignore
	private static final ClientPingRequest instance = new ClientPingRequest();

	@Protobuf(description = "客户端自定义数据. 服务器会在pong返回")
	private byte[] bytes;

	public static ClientPingRequest valueOf(){
		return instance;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
}
