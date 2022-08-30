package org.qiunet.function.gm.proto.req;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.util.proto.SkipProtoGenerator;

import java.util.List;

/***
 * gm 命令请求
 *
 * @author qiunet
 * 2021-01-08 12:58
 */
@SkipProtoGenerator
@ChannelData(ID = IProtocolId.System.GM_dTOOLS_COMMAND_REQ, desc = "处理dTools gm请求")
public class GmDToolsCommandReq extends IChannelData {
	@Protobuf(description="处理的id")
	private long playerId;
	@Protobuf(description = "类型")
	private int type;
	@Protobuf(description = "参数值")
	private List<String> params;

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
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
