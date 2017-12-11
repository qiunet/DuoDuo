package org.qiunet.test.server.login;

import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequest;
import org.qiunet.flash.handler.handler.tcp.TcpProtobufHandler;
import org.qiunet.test.proto.LoginOnlineProto;
import org.qiunet.test.proto.LoginProto;

/**
 * Created by qiunet.
 * 17/12/8
 */
@RequestHandler(ID = 1001, desc = "登录Online服务")
public class LoginOnlineHandler extends TcpProtobufHandler<LoginOnlineProto.LoginOnlineRequest> {

	@Override
	public void handler(ITcpRequest<LoginOnlineProto.LoginOnlineRequest> context) {

		logger.info("LoginOnlineHandler: "+ context.getRequestData());

		LoginProto.LoginResponse response = LoginProto.LoginResponse.newBuilder().build();
		context.response(1000000, response);
	}

	@Override
	public boolean needToken() {
		return true;
	}
}
