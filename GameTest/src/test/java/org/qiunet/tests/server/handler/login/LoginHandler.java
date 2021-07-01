package org.qiunet.tests.server.handler.login;

import org.qiunet.flash.handler.context.request.http.IHttpRequest;
import org.qiunet.flash.handler.handler.http.HttpProtobufHandler;
import org.qiunet.tests.proto.LoginRequest;
import org.qiunet.tests.proto.LoginResponse;
import org.qiunet.tests.server.handler.ServerUidAndTokenBuilder;

/**
 * Created by qiunet.
 * 17/12/8
 */
public class LoginHandler extends HttpProtobufHandler<LoginRequest, LoginResponse> {
	@Override
	public LoginResponse handler(IHttpRequest<LoginRequest> request)throws Exception {
		logger.info("LoginHandler: "+request.getRequestData()+ " Address: "+request.getRemoteAddress());

		return  LoginResponse.valueOf(ServerUidAndTokenBuilder.getUid(request.getRequestData().getOpenId()),
			ServerUidAndTokenBuilder.getToken(request.getRequestData().getOpenId()), true);
	}

	@Override
	public boolean needAuth() {
		return false;
	}
}
