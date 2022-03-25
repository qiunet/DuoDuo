package org.qiunet.test.handler.handler.http;

import org.qiunet.flash.handler.common.annotation.UriPathHandler;
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
	public void handler(HttpAsyncTask<JTestRequestData, JTestResponseData> asyncTask) throws Exception {
		JTestRequestData request = asyncTask.getRequest();
		asyncTask.response(new JTestResponseData(request.getTest()));
	}
}
