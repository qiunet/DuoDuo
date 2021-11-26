package org.qiunet.test.handler.handler.http;

import org.qiunet.flash.handler.common.annotation.UriPathHandler;
import org.qiunet.flash.handler.context.request.http.IHttpRequest;
import org.qiunet.flash.handler.handler.http.HttpStringHandler;
import org.qiunet.flash.handler.handler.http.ISyncHttpHandler;

/**
 * Created by qiunet.
 * 17/11/23
 */
@UriPathHandler("/back")
public class UriHandler extends HttpStringHandler implements ISyncHttpHandler<String, String> {
	@Override
	public String handler(IHttpRequest<String> request) throws Exception {
		return request.getRequestData();
	}
}
