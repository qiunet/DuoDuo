package org.qiunet.flash.handler.handler.tcp;

import org.qiunet.flash.handler.common.enums.HandlerType;
import org.qiunet.flash.handler.handler.BaseHandler;

/**
 * Created by qiunet.
 * 17/7/21
 */
abstract class BaseTcpHandler<RequestData> extends BaseHandler<RequestData> implements ITcpHandler<RequestData> {
	@Override
	public HandlerType getHandlerType() {
		return HandlerType.TCP;
	}
}
