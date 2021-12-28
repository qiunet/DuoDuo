package org.qiunet.function.gm.proto.rsp;

import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

/***
 *
 * @author qiunet
 * 2021/12/27 21:28
 */
@ChannelData(ID = IProtocolId.System.GM_DEBUG_PROTOCOL_RSP, desc = "协议调试完成响应")
public class GmDebugProtocolRsp implements IChannelData {

	public static GmDebugProtocolRsp valueOf() {
		return new GmDebugProtocolRsp();
	}
}
