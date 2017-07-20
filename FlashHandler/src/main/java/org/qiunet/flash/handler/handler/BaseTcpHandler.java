package org.qiunet.flash.handler.handler;

import org.qiunet.flash.handler.common.enums.HandlerType;

/**
 * Created by qiunet.
 * 17/7/21
 */
public abstract class BaseTcpHandler<RequestData> extends BaseHandler<RequestData> implements ITcpHandler<RequestData> {
	@Override
	public HandlerType getHandlerType() {
		return HandlerType.TCP;
	}
}
