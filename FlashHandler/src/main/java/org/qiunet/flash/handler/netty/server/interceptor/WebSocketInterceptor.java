package org.qiunet.flash.handler.netty.server.interceptor;

import org.qiunet.flash.handler.context.request.websocket.IWebSocketRequest;
import org.qiunet.flash.handler.handler.websocket.IWebSocketHandler;

/**
 * Created by qiunet.
 * 17/12/2
 */
public interface WebSocketInterceptor {
	/***
	 * 自行处理
	 * @param handler
	 * @param request
	 */
	public void handler(IWebSocketHandler handler, IWebSocketRequest request);
}
