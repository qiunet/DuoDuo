package org.qiunet.flash.handler.common.player.proto;

import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

/***
 *
 * @author qiunet
 * 2023/2/6 20:47
 */
@ChannelData(ID = IProtocolId.System.LOGOUT_REQ, desc = "登出协议")
public class LogoutReq extends IChannelData {
}
