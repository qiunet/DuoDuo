package org.qiunet.flash.handler.handler.http;

import org.qiunet.flash.handler.common.annotation.OtherRequestHandler;
import org.qiunet.flash.handler.context.request.http.IHttpRequest;

/**
 * Created by qiunet.
 * 17/11/23
 */
@OtherRequestHandler(uriPath = "/back")
public class TestUriHandler extends HttpStringHandler {
	@Override
	public String handler(IHttpRequest<String> request) {
		return null;
	}
}
