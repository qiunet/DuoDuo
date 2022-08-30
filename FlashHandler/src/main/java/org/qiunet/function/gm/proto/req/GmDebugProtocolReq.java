package org.qiunet.function.gm.proto.req;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

/***
 *
 * @author qiunet
 * 2021/12/27 21:28
 */
@SkipDebugOut
@ChannelData(ID = IProtocolId.System.GM_DEBUG_PROTOCOL_REQ, desc = "协议调试")
public class GmDebugProtocolReq extends IChannelData {
	@Protobuf(description = "要调试的协议ID")
	private int protocolId;
	@Protobuf(description = "协议的json内容")
	private String data;

	public static GmDebugProtocolReq valueOf(int protocolId, String jsonData) {
		GmDebugProtocolReq data = new GmDebugProtocolReq();
		data.protocolId = protocolId;
		data.data = jsonData;
		return data;
	}

	public int getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(int protocolId) {
		this.protocolId = protocolId;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
