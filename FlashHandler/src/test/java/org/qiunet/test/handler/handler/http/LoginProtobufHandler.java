package org.qiunet.test.handler.handler.http;

import org.qiunet.flash.handler.context.request.http.IAsyncHttpRequest;
import org.qiunet.flash.handler.handler.http.HttpProtobufHandler;
import org.qiunet.flash.handler.handler.http.IAsyncHttpHandler;
import org.qiunet.test.handler.proto.HttpPbLoginRequest;
import org.qiunet.test.handler.proto.LoginResponse;

/**
 * Created by qiunet.
 * 17/11/21
 */
public class LoginProtobufHandler extends HttpProtobufHandler<HttpPbLoginRequest, LoginResponse> implements IAsyncHttpHandler<HttpPbLoginRequest, LoginResponse> {
//
//	@Override
//	public LoginResponse handler(IHttpRequest<HttpPbLoginRequest> request) throws Exception {
//		return LoginResponse.valueOf(request.getRequestData().getAccount());
//	}

	@Override
	public void handler(IAsyncHttpRequest<HttpPbLoginRequest, LoginResponse> request) throws Exception {
		request.response(LoginResponse.valueOf(request.getRequestData().getAccount()));
	}
}
