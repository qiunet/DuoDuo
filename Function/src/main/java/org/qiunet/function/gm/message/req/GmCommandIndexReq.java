package org.qiunet.function.gm.message.req;

import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelData;

/***
 * gm 命令首页请求
 *
 * @author qiunet
 * 2021-01-08 12:58
 */
@PbChannelData(ID = IProtocolId.System.GM_COMMAND_INDEX_REQ, desc = "gm 命令首页")
public class GmCommandIndexReq implements IpbChannelData {
}
