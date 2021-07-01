package org.qiunet.function.gm.message.req;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelData;

import java.util.List;

/***
 * gm 命令请求
 *
 * @author qiunet
 * 2021-01-08 12:58
 */
@PbChannelData(ID = IProtocolId.System.GM_COMMAND_REQ, desc = "处理gm请求")
public class GmCommandReq implements IpbChannelData {
	@Protobuf(description = "类型")
	private int type;
	@Protobuf(description = "参数值")
	private List<String> params;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<String> getParams() {
		return params;
	}

	public void setParams(List<String> params) {
		this.params = params;
	}
}
