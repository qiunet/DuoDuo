package org.qiunet.test.handler.handler.http;

import org.qiunet.flash.handler.handler.http.HttpProtobufHandler;
import org.qiunet.flash.handler.handler.http.async.HttpAsyncTask;
import org.qiunet.flash.handler.handler.http.async.IAsyncHttpHandler;
import org.qiunet.test.handler.proto.HttpPbLoginRequest;
import org.qiunet.test.handler.proto.LoginResponse;

/**
 *
 * Created by qiunet.
 * 17/11/21
 */
public class LoginProtobufHandler extends HttpProtobufHandler<HttpPbLoginRequest>
		implements IAsyncHttpHandler<HttpPbLoginRequest, LoginResponse> {

	@Override
	public void handler(HttpAsyncTask<HttpPbLoginRequest, LoginResponse> asyncTask) throws Exception {
		HttpPbLoginRequest request = asyncTask.getRequest();
		asyncTask.response(LoginResponse.valueOf(request.getAccount()));
	}
}
