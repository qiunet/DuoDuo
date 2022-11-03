package org.qiunet.function.prometheus;

import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.common.annotation.UriPathHandler;
import org.qiunet.flash.handler.context.request.http.IHttpRequest;
import org.qiunet.flash.handler.handler.http.HttpStringHandler;
import org.qiunet.flash.handler.handler.http.ISyncHttpHandler;

/***
 * prometheus 请求
 * @author qiunet
 * 2022/11/2 16:40
 */
@SkipDebugOut
@UriPathHandler("/metrics")
public class PrometheusHandler extends HttpStringHandler implements ISyncHttpHandler<String, String> {

	@Override
	public String handler(IHttpRequest<String> request) throws Exception {
		return RootRegistry.instance.scrape();
	}
}
