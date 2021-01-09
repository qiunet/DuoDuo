package org.qiunet.function.gm.message.resp;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.pb.IpbResponseData;
import org.qiunet.flash.handler.context.request.data.pb.PbResponse;

/***
 *执行结果
 *
 * @author qiunet
 * 2021-01-09 11:08
 */
@ProtobufClass(description = "执行结果响应")
@PbResponse(IProtocolId.System.GM_COMMAND_RESP)
public class GmCommandResp implements IpbResponseData {
	@Protobuf(description = "执行结果")
	private boolean success;

	public static GmCommandResp valueOf(boolean success) {
		GmCommandResp resp = new GmCommandResp();
		resp.success = success;
		return resp;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
