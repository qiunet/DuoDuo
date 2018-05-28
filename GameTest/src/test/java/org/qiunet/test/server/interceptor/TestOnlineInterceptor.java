package org.qiunet.test.server.interceptor;

import org.qiunet.flash.handler.context.request.websocket.IWebSocketRequest;
import org.qiunet.flash.handler.handler.websocket.IWebSocketHandler;
import org.qiunet.flash.handler.netty.server.interceptor.WebSocketInterceptor;

/**
 * Created by qiunet.
 * 17/12/11
 */
public class TestOnlineInterceptor implements WebSocketInterceptor {

	@Override
	public void handler(IWebSocketHandler handler, IWebSocketRequest request) {
		try {
			handler.handler(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
