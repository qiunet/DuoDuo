package org.qiunet.flash.handler.handler.http;

import org.qiunet.flash.handler.common.annotation.UriPathHandler;
import org.qiunet.flash.handler.context.request.http.IHttpRequest;

/**
 * Created by qiunet.
 * 17/11/23
 */
@UriPathHandler("/back")
public class UriHandler extends HttpStringHandler {
	@Override
	public String handler(IHttpRequest<String> request) throws Exception {
		return request.getRequestData();
	}
}
