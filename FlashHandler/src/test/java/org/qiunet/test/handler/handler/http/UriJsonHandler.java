package org.qiunet.test.handler.handler.http;

import org.qiunet.flash.handler.common.annotation.UriPathHandler;
import org.qiunet.flash.handler.context.request.http.IHttpRequest;
import org.qiunet.flash.handler.context.request.http.json.JsonRequest;
import org.qiunet.flash.handler.context.response.json.JsonResponse;
import org.qiunet.flash.handler.handler.http.HttpJsonHandler;

/**
 * Created by qiunet.
 * 18/1/29
 */
@UriPathHandler("/jsonUrl")
public class UriJsonHandler extends HttpJsonHandler {
	@Override
	protected JsonResponse handler0(IHttpRequest<JsonRequest> request) {
		JsonResponse response = new JsonResponse();
		response.addAttribute("test", request.getRequestData().getString("test"));
		return response;
	}
}
