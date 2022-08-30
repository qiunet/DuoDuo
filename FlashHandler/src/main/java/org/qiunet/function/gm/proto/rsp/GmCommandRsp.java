package org.qiunet.function.gm.proto.rsp;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.status.IGameStatus;

/***
 *执行结果
 *
 * @author qiunet
 * 2021-01-09 11:08
 */
@ChannelData(ID = IProtocolId.System.GM_COMMAND_RSP, desc = "执行结果响应")
public class GmCommandRsp extends IChannelData {
	@Protobuf(description = "执行结果")
	private int status;

	public static GmCommandRsp valueOf(IGameStatus status) {
		GmCommandRsp resp = new GmCommandRsp();
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
