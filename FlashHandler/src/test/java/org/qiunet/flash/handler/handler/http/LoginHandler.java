package org.qiunet.flash.handler.handler.http;

import org.qiunet.flash.handler.context.request.http.IHttpRequest;
import org.qiunet.flash.handler.handler.string.HttpStringHandler;

/**
 * Created by qiunet.
 * 17/11/21
 */
public class LoginHandler extends HttpStringHandler {
	@Override
	public String handler(IHttpRequest<String> request) {
		String str = request.getRequestData();
		return null;
	}
}
