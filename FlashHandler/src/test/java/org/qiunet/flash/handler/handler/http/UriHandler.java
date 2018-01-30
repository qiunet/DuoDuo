package org.qiunet.flash.handler.handler.http;

import org.qiunet.flash.handler.common.annotation.UriPathRequestHandler;
import org.qiunet.flash.handler.context.request.http.IHttpRequest;

/**
 * Created by qiunet.
 * 17/11/23
 */
@UriPathRequestHandler(uriPath = "/back")
public class UriHandler extends HttpStringHandler {
	@Override
	public String handler(IHttpRequest<String> request) {
		return request.getRequestData();
	}
}
