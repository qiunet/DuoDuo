package org.qiunet.test.handler.handler.http;

import org.qiunet.flash.handler.common.annotation.UriPathHandler;
import org.qiunet.flash.handler.context.request.http.IHttpRequest;
import org.qiunet.flash.handler.handler.http.HttpJsonHandler;
import org.qiunet.flash.handler.handler.http.async.HttpAsyncTask;
import org.qiunet.flash.handler.handler.http.async.IAsyncHttpHandler;

/**
 * Created by qiunet.
 * 18/1/29
 */
@UriPathHandler("/jsonUrl")
public class UriJsonHandler extends HttpJsonHandler<JTestRequestData> implements IAsyncHttpHandler<JTestRequestData, JTestResponseData> {
	@Override
	public HttpAsyncTask<JTestResponseData> handler(IHttpRequest<JTestRequestData> request) throws Exception {
		return new HttpAsyncTask<>(() -> {
			return new JTestResponseData(request.getRequestData().getTest());
		});
	}
}
