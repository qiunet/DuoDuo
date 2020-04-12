package org.qiunet.flash.handler.handler.websocket;

import org.qiunet.flash.handler.common.enums.HandlerType;
import org.qiunet.flash.handler.common.player.IPlayerActor;
import org.qiunet.flash.handler.handler.BaseHandler;

/**
 * Created by qiunet.
 * 17/7/21
 */
abstract class BaseWebSocketHandler<P extends IPlayerActor, RequestData> extends BaseHandler<RequestData> implements IWebSocketHandler<P, RequestData> {
	@Override
	public HandlerType getHandlerType() {
		return HandlerType.WEB_SOCKET;
	}
}
