package org.qiunet.function.gm.proto.req;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

import java.util.List;

/***
 * gm 命令请求
 *
 * @author qiunet
 * 2021-01-08 12:58
 */
@ChannelData(ID = IProtocolId.System.GM_COMMAND_REQ, desc = "处理gm请求")
public class GmCommandReq extends IChannelData {
	@Protobuf(description = "类型")
	private int type;
	@Protobuf(description = "参数值")
	private List<String> params;

	public static GmCommandReq valueOf(int type, List<String> params) {
		GmCommandReq data = new GmCommandReq();
		data.type = type;
		data.params = params;
		return data;
	}

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
