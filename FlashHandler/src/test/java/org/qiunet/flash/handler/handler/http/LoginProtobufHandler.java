package org.qiunet.flash.handler.handler.http;

import org.qiunet.flash.handler.context.request.http.IHttpRequest;
import org.qiunet.flash.handler.handler.proto.LoginProto;

/**
 * Created by qiunet.
 * 17/11/21
 */
public class LoginProtobufHandler extends HttpProtobufHandler<LoginProto.LoginRequest, LoginProto.LoginResponse> {
	@Override
	public LoginProto.LoginResponse handler(IHttpRequest<LoginProto.LoginRequest> request) {

		return null;
	}
}
