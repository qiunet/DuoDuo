package org.qiunet.flash.handler.netty.server.param.adapter.message;

import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelData;

/***
 *
 *
 * @author qiunet
 * 2020-09-25 16:59
 */
@PbChannelData(ID = IProtocolId.System.HANDLER_NOT_FIND, desc = "没有处理protocolID的Handler错误")
public class HandlerNotFoundResponse implements IpbChannelData {
}
