package org.qiunet.flash.handler.netty.server.param.adapter.message;

import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

/***
 * 服务未开启响应
 *
 * @author qiunet
 * 2020-11-11 10:04
 */
@ChannelData(ID = IProtocolId.System.SERVER_NOT_OPEN_RESP, desc = "服务未开启响应")
public class ServerCloseResponse implements IChannelData {
	/**
	 * 停服公告
	 * 停服公告由 {@link org.qiunet.utils.listener.event.data.ServerClosedEvent} 触发放入.
	 */
	@Ignore
	public static String closeMsg;

	@Protobuf(description = "服务没有开启提示")
	private String serverCloseMsg;

	public static ServerCloseResponse valueOf() {
		ServerCloseResponse response = new ServerCloseResponse();
		response.serverCloseMsg = closeMsg;
		return response;

	}

	public String getServerCloseMsg() {
		return serverCloseMsg;
	}

	public void setServerCloseMsg(String serverCloseMsg) {
		this.serverCloseMsg = serverCloseMsg;
	}
}
