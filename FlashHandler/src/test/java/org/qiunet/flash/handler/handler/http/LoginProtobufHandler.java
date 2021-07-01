package org.qiunet.flash.handler.handler.http;

import org.qiunet.flash.handler.context.request.http.IHttpRequest;
import org.qiunet.flash.handler.proto.HttpPbLoginRequest;
import org.qiunet.flash.handler.proto.LoginResponse;

/**
 * Created by qiunet.
 * 17/11/21
 */
public class LoginProtobufHandler extends HttpProtobufHandler<HttpPbLoginRequest, LoginResponse> {

	@Override
	public LoginResponse handler(IHttpRequest<HttpPbLoginRequest> request) throws Exception {
		return LoginResponse.valueOf(request.getRequestData().getAccount());
	}
}
