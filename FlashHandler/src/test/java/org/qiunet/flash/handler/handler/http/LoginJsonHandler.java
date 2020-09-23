package org.qiunet.flash.handler.handler.http;

import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.http.IHttpRequest;
import org.qiunet.flash.handler.context.request.http.json.JsonRequest;
import org.qiunet.flash.handler.context.response.json.JsonResponse;

/**
 * Created by qiunet.
 * 18/1/29
 */
@RequestHandler(ID = 1007, desc = "json")
public class LoginJsonHandler extends HttpJsonHandler {

	@Override
	protected JsonResponse handler0(IHttpRequest<JsonRequest> request) {
		JsonResponse response = new JsonResponse();
		response.addAttribute("test", request.getRequestData().getString("test"));
		return response;
	}
}
