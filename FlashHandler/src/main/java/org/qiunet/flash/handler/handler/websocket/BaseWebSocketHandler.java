package org.qiunet.flash.handler.handler.websocket;

import org.qiunet.flash.handler.common.enums.HandlerType;
import org.qiunet.flash.handler.handler.BaseHandler;

/**
 * Created by qiunet.
 * 17/7/21
 */
abstract class BaseWebSocketHandler<RequestData> extends BaseHandler<RequestData> implements IWebSocketHandler<RequestData> {
	@Override
	public HandlerType getHandlerType() {
		return HandlerType.WEB_SOCKET;
	}
}
