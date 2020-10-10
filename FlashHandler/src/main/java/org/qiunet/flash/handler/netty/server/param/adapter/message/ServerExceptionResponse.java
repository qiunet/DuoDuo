package org.qiunet.flash.handler.netty.server.param.adapter.message;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.flash.handler.context.request.data.pb.IpbResponseData;
import org.qiunet.flash.handler.context.request.data.pb.PbResponse;
import org.qiunet.flash.handler.context.response.push.DefaultProtobufMessage;

/***
 *
 *
 * @author qiunet
 * 2020-09-25 16:59
 */
@PbResponse(500)
@ProtobufClass(description = "服务器异常")
public class ServerExceptionResponse implements IpbResponseData {

	public static final ServerExceptionResponse DEFAULT_INSTANCE = new ServerExceptionResponse();

	public static final DefaultProtobufMessage DEFAULT_MESSAGE = DEFAULT_INSTANCE.buildResponseMessage();
}
