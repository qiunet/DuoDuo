package org.qiunet.flash.handler.netty.server.param.adapter.message;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.pb.IpbResponseData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelDataID;

/***
 * 服务未开启响应
 *
 * @author qiunet
 * 2020-11-11 10:04
 */
@ProtobufClass(description = "服务未开启响应")
@PbChannelDataID(IProtocolId.System.SERVER_NOT_OPEN_RESP)
public class ServerCloseResponse implements IpbResponseData {
	@Protobuf(description = "服务没有开启提示")
	private String serverCloseMsg;

	public static ServerCloseResponse valueOf(String serverCloseMsg) {
		ServerCloseResponse response = new ServerCloseResponse();
		response.serverCloseMsg = serverCloseMsg;
		return response;

	}

	public String getServerCloseMsg() {
		return serverCloseMsg;
	}

	public void setServerCloseMsg(String serverCloseMsg) {
		this.serverCloseMsg = serverCloseMsg;
	}
}
