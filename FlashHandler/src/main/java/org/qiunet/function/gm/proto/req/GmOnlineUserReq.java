package org.qiunet.function.gm.proto.req;

import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.util.proto.SkipProtoGenerator;

import static org.qiunet.flash.handler.common.id.IProtocolId.System.GM_ONLINE_USER_REQ;

/***
 *
 * @author qiunet
 * 2022/3/7 11:21
 */
@SkipProtoGenerator
@ChannelData(ID = GM_ONLINE_USER_REQ, desc = "在线玩家请求")
public class GmOnlineUserReq extends IChannelData {
}
