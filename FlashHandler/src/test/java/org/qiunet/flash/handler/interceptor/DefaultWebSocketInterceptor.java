package org.qiunet.flash.handler.interceptor;

import org.qiunet.flash.handler.context.request.websocket.IWebSocketRequest;
import org.qiunet.flash.handler.handler.websocket.IWebSocketHandler;
import org.qiunet.flash.handler.netty.server.interceptor.WebSocketInterceptor;
import org.qiunet.utils.logger.LoggerManager;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.logger.log.QLogger;

/**
 * Created by qiunet.
 * 17/12/2
 */
public class DefaultWebSocketInterceptor implements WebSocketInterceptor {
	private static QLogger logger = LoggerManager.getLogger(LoggerType.FLASH_HANDLER);

	@Override
	public void handler(IWebSocketHandler handler, IWebSocketRequest request) {
		logger.info("webSocket received message: "+request.getRequestData());
		handler.handler(request);
	}
}
