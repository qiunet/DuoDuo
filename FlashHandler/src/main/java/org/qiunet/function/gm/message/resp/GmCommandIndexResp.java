package org.qiunet.function.gm.message.resp;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

import java.util.List;

/***
 * gm 首页响应
 *
 * @author qiunet
 * 2021-01-09 10:35
 */
@ChannelData(ID = IProtocolId.System.GM_COMMAND_LIST_RESP, desc = "gm 首页响应")
public class GmCommandIndexResp implements IChannelData {
	@Protobuf(description = "所有gm命令列表")
	private List<GmCommandInfo> list;

	public static GmCommandIndexResp valueOf(List<GmCommandInfo> list) {
		GmCommandIndexResp resp = new GmCommandIndexResp();
		resp.list = list;
		return resp;
	}

	public List<GmCommandInfo> getList() {
		return list;
	}

	public void setList(List<GmCommandInfo> list) {
		this.list = list;
	}
}
