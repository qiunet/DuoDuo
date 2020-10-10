package org.qiunet.flash.handler.netty.server.param.adapter.message;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.pb.IpbResponseData;
import org.qiunet.flash.handler.context.request.data.pb.PbResponse;

/***
 *
 *
 * @author qiunet
 * 2020-09-25 16:59
 */
@PbResponse(IProtocolId.System.HANDLER_NOT_FIND)
@ProtobufClass(description = "没有处理protocolID的Handler错误")
public class HandlerNotFoundResponse implements IpbResponseData {
}
