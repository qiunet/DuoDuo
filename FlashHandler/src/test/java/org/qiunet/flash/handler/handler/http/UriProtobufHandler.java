package org.qiunet.flash.handler.handler.http;

import org.qiunet.flash.handler.common.annotation.UriPathHandler;
import org.qiunet.flash.handler.context.request.http.IHttpRequest;
import org.qiunet.flash.handler.proto.LoginRequest;
import org.qiunet.flash.handler.proto.LoginResponse;

/**
 * Created by qiunet.
 * 17/11/25
 */
@UriPathHandler("/protobufTest")
public class UriProtobufHandler extends HttpProtobufHandler<LoginRequest, LoginResponse> {


	@Override
	public LoginResponse handler(IHttpRequest<LoginRequest> request) throws Exception {
		return LoginResponse.valueOf(request.getRequestData().getAccount());
	}
}
