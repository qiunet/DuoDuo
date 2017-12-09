package org.qiunet.test.server.login;

import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.http.IHttpRequest;
import org.qiunet.flash.handler.handler.http.HttpProtobufHandler;
import org.qiunet.test.proto.LoginProto;
import org.qiunet.test.server.ServerUidAndTokenBuilder;

/**
 * Created by qiunet.
 * 17/12/8
 */
@RequestHandler(ID = 1001, desc = "http 版本的登录")
public class LoginHttpHandler extends HttpProtobufHandler<LoginProto.LoginRequest , LoginProto.LoginResponse> {
	@Override
	public LoginProto.LoginResponse handler(IHttpRequest<LoginProto.LoginRequest> request) {
		logger.info("LoginHttpHandler: "+request.getRequestData()+ " Address: "+request.getRemoteAddress());

		return  LoginProto.LoginResponse.newBuilder()
				.setUid(ServerUidAndTokenBuilder.getUid(request.getRequestData().getOpenid()))
				.setToken(ServerUidAndTokenBuilder.getToken(request.getRequestData().getOpenid()))
				.setRegistered(true)
				.build();
	}
}
