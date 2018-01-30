package org.qiunet.test.server.handler.player;

import org.qiunet.flash.handler.common.annotation.UriPathRequestHandler;
import org.qiunet.flash.handler.context.request.http.IHttpRequest;
import org.qiunet.flash.handler.context.request.http.json.JsonRequest;
import org.qiunet.flash.handler.context.response.json.JsonResponse;
import org.qiunet.flash.handler.handler.http.HttpJsonHandler;

/**
 * Created by qiunet.
 * 18/1/30
 */
@UriPathRequestHandler(uriPath = "playerInfo")
public class PlayerInfoJsonHandler extends HttpJsonHandler {
	@Override
	protected JsonResponse handler1(IHttpRequest<JsonRequest> request) {
		JsonResponse response = new JsonResponse();

		return response.addAttribute("token", request.getRequestData().getString("token"));
	}
}
