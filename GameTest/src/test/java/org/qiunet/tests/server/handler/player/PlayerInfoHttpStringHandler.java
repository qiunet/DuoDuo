package org.qiunet.tests.server.handler.player;

import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.http.IHttpRequest;
import org.qiunet.flash.handler.handler.http.HttpStringHandler;
/**
 * Created by qiunet.
 * 18/1/30
 */
@RequestHandler(ID = 1004, desc = "")
public class PlayerInfoHttpStringHandler extends HttpStringHandler {

	@Override
	public String handler(IHttpRequest<String> request)throws Exception {
		return request.getRequestData();
	}
}
