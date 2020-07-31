package org.qiunet.flash.handler.handler.tcp;

import org.qiunet.flash.handler.common.enums.HandlerType;
import org.qiunet.flash.handler.common.player.IPlayerActor;
import org.qiunet.flash.handler.handler.BaseHandler;

/**
 * Created by qiunet.
 * 17/7/21
 */
abstract class BaseTcpHandler<P extends IPlayerActor, RequestData> extends BaseHandler<RequestData> implements ITcpHandler<P, RequestData> {
	@Override
	public HandlerType getHandlerType() {
		return HandlerType.TCP;
	}
}
