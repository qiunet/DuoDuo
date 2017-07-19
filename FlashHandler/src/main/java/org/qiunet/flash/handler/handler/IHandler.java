package org.qiunet.flash.handler.handler;

import org.qiunet.flash.handler.common.enums.HandlerType;

/**
 * Handler 的接口, 下面会分两个分支出来
 * Created by qiunet.
 * 17/7/19
 */
public interface IHandler<RequestData> {
	/**
	 * 得到handlerType
	 * @see HandlerType
	 * @return
	 */
	public HandlerType getHandlerType();
	/**
	 * 得到 RequestID
	 * @return
	 */
	public int getRequestID();
	/**
	 * 必须要sid 才能进入action
	 * @return
	 */
	public boolean needSid();
	/***
	 * 得到requestData的class
	 */
	public Class<RequestData> getRequestClass();
}
