package org.qiunet.flash.handler.handler.udp;

import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequest;
import org.qiunet.flash.handler.context.request.udp.IUdpRequest;
import org.qiunet.flash.handler.context.response.push.DefaultProtobufMessage;
import org.qiunet.flash.handler.context.response.push.ResponseMsgUtil;
import org.qiunet.flash.handler.handler.proto.LoginProto;
import org.qiunet.flash.handler.handler.tcp.TcpProtobufHandler;

/**
 * Created by qiunet.
 * 17/8/16
 */
@RequestHandler(ID = 1009, desc = "protobuf 测试")
public class LoginProtobufHandler extends UdpProtobufHandler<LoginProto.LoginRequest> {

	@Override
	public void handler(IUdpRequest<LoginProto.LoginRequest> context)throws Exception {
		context.udpResponse(2001, LoginProto.LoginResponse.newBuilder().setTestString(context.getRequestData().getTestString()).build());
	}
}
