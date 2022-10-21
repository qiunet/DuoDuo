package org.qiunet.function.gm.proto.rsp;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.util.proto.SkipProtoGenerator;


/***
 *
 * @author qiunet
 * 2022/3/8 20:03
 */
@SkipProtoGenerator
@ChannelData(ID = IProtocolId.System.GM_dTOOLS_COMMAND_RSP, desc = "dTools 响应")
public class GmDToolsCommandRsp extends IChannelData {
	@Protobuf(description="是否成功")
	private boolean success;
	@Protobuf(description="错误消息")
	private String errMsg;

	public GmDToolsCommandRsp() {
	}

	public GmDToolsCommandRsp(boolean success, String errMsg) {
		this.success = success;
		this.errMsg = errMsg;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}

