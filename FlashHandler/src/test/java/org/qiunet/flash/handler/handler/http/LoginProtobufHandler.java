package org.qiunet.flash.handler.handler.http;

import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.http.IHttpRequest;
import org.qiunet.flash.handler.proto.LoginRequest;
import org.qiunet.flash.handler.proto.LoginResponse;

/**
 * Created by qiunet.
 * 17/11/21
 */
@RequestHandler(ID = 1001, desc = "login protobuf type")
public class LoginProtobufHandler extends HttpProtobufHandler<LoginRequest, LoginResponse> {

	@Override
	public LoginResponse handler(IHttpRequest<LoginRequest> request) throws Exception {
		return LoginResponse.valueOf(request.getRequestData().getAccount());
	}
}
