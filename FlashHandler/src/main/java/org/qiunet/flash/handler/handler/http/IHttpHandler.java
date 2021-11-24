package org.qiunet.flash.handler.handler.http;

import org.qiunet.flash.handler.common.enums.HandlerType;
import org.qiunet.flash.handler.handler.IHandler;

/***
 *
 * @author qiunet
 * 2021/11/24 17:46
 */
public interface IHttpHandler<RequestData> extends IHandler<RequestData> {
	/**
	 * 是否是异步处理
	 * @return
	 */
	boolean isAsync();

	/**
	 * 一般用于http协议. true的情况不允许频繁访问.
	 * @return
	 */
	default boolean fastRequestControl() {
		return true;
	}

	@Override
	default HandlerType getHandlerType() {
		return HandlerType.HTTP;
	}
}
