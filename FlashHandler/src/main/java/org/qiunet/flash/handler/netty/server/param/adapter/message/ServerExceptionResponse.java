package org.qiunet.flash.handler.netty.server.param.adapter.message;

import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

/***
 *
 *
 * @author qiunet
 * 2020-09-25 16:59
 */
@ChannelData(ID = IProtocolId.System.SERVER_EXCEPTION, desc = "服务器异常")
public class ServerExceptionResponse extends IChannelData {

}
