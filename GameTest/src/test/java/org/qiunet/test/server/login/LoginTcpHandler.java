package org.qiunet.test.server.login;

import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequest;
import org.qiunet.flash.handler.handler.tcp.TcpProtobufHandler;
import org.qiunet.test.proto.LoginProto;
import org.qiunet.test.server.ServerUidAndTokenBuilder;

/**
 * Created by qiunet.
 * 17/12/8
 */
@RequestHandler(ID = 1000, desc = "登录")
public class LoginTcpHandler extends TcpProtobufHandler<LoginProto.LoginRequest> {

	@Override
	public void handler(ITcpRequest<LoginProto.LoginRequest> context) {

		logger.info("LoginTcpHandler: "+ context.getRequestData());

		LoginProto.LoginResponse response = LoginProto.LoginResponse.newBuilder()
				.setUid(ServerUidAndTokenBuilder.getUid(context.getRequestData().getOpenid()))
				.setToken(ServerUidAndTokenBuilder.getToken(context.getRequestData().getOpenid()))
				.setRegistered(true)
				.build();
		context.response(1000000, response);
	}
}
