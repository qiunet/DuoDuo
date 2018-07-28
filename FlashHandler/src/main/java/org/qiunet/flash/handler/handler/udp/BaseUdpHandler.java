package org.qiunet.flash.handler.handler.udp;

import org.qiunet.flash.handler.common.enums.HandlerType;
import org.qiunet.flash.handler.context.request.udp.IUdpRequest;
import org.qiunet.flash.handler.handler.BaseHandler;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/7/28 11:13
 **/
public abstract class BaseUdpHandler<RequestData> extends BaseHandler<RequestData> implements IUdpRequest<RequestData> {
	@Override
	public HandlerType getHandlerType() {
		return HandlerType.UDP;
	}
}
