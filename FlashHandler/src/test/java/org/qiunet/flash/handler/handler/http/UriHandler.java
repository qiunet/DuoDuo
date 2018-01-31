package org.qiunet.flash.handler.handler.http;

import org.qiunet.flash.handler.common.annotation.UriPathRequest;
import org.qiunet.flash.handler.context.request.http.IHttpRequest;

/**
 * Created by qiunet.
 * 17/11/23
 */
@UriPathRequest("/back")
public class UriHandler extends HttpStringHandler {
	@Override
	public String handler(IHttpRequest<String> request) {
		return request.getRequestData();
	}
}
