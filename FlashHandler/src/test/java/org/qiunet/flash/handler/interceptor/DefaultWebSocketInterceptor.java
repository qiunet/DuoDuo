package org.qiunet.flash.handler.interceptor;

import org.qiunet.flash.handler.context.request.websocket.IWebSocketRequest;
import org.qiunet.flash.handler.handler.websocket.IWebSocketHandler;
import org.qiunet.flash.handler.netty.server.interceptor.WebSocketInterceptor;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by qiunet.
 * 17/12/2
 */
public class DefaultWebSocketInterceptor implements WebSocketInterceptor {
	private static Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);

	@Override
	public void handler(IWebSocketHandler handler, IWebSocketRequest request) {
		logger.info("webSocket received message: "+request.getRequestData());
		try {
			handler.handler(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
