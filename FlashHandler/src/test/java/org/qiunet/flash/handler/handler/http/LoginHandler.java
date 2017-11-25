package org.qiunet.flash.handler.handler.http;

import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.http.IHttpRequest;

/**
 * Created by qiunet.
 * 17/11/21
 */
@RequestHandler(ID = 1000, desc = "login string type")
public class LoginHandler extends HttpStringHandler {
	@Override
	public String handler(IHttpRequest<String> request) {
		String str = request.getRequestData();
		return str;
	}
}
