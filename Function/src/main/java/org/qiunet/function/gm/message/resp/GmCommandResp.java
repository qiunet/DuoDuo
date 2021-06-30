package org.qiunet.function.gm.message.resp;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.pb.IpbResponseData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelDataID;
import org.qiunet.flash.handler.context.status.IGameStatus;

/***
 *执行结果
 *
 * @author qiunet
 * 2021-01-09 11:08
 */
@ProtobufClass(description = "执行结果响应")
@PbChannelDataID(IProtocolId.System.GM_COMMAND_RESP)
public class GmCommandResp implements IpbResponseData {
	@Protobuf(description = "执行结果")
	private int status;

	public static GmCommandResp valueOf(IGameStatus status) {
		GmCommandResp resp = new GmCommandResp();
		resp.status = status.getStatus();
		return resp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
