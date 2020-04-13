package org.qiunet.tests.server.handler.login;

import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.http.IHttpRequest;
import org.qiunet.flash.handler.handler.http.HttpProtobufHandler;
import org.qiunet.tests.proto.LoginProto;
import org.qiunet.tests.server.handler.ServerUidAndTokenBuilder;

/**
 * Created by qiunet.
 * 17/12/8
 */
@RequestHandler(ID = 1000, desc = "http登录")
public class LoginHandler extends HttpProtobufHandler<LoginProto.LoginRequest , LoginProto.LoginResponse> {
	@Override
	public LoginProto.LoginResponse handler(IHttpRequest<LoginProto.LoginRequest> request)throws Exception {
		logger.info("LoginHandler: "+request.getRequestData()+ " Address: "+request.getRemoteAddress());

		return  LoginProto.LoginResponse.newBuilder()
				.setUid(ServerUidAndTokenBuilder.getUid(request.getRequestData().getOpenid()))
				.setToken(ServerUidAndTokenBuilder.getToken(request.getRequestData().getOpenid()))
				.setRegistered(true)
				.build();
	}

	@Override
	public boolean needAuth() {
		return false;
	}
}
