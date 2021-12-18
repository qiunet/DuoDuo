package org.qiunet.test.handler.handler.http;

import com.google.common.base.Preconditions;
import org.qiunet.flash.handler.common.annotation.UriPathHandler;
import org.qiunet.flash.handler.context.request.http.IHttpRequest;
import org.qiunet.flash.handler.handler.http.HttpStringHandler;
import org.qiunet.flash.handler.handler.http.ISyncHttpHandler;

/**
 * Created by qiunet.
 * 17/11/23
 */
@UriPathHandler(value = "/back", post_params = true)
public class UriHandler extends HttpStringHandler implements ISyncHttpHandler<String, String> {
	@Override
	public String handler(IHttpRequest<String> request) throws Exception {
		Preconditions.checkState(request.getParameter("p1").equals("val1"));
		Preconditions.checkState(request.getParameter("p2").equals("val2"));
		return request.getRequestData();
	}
}
