package org.qiunet.flash.handler.handler;

import org.qiunet.flash.handler.common.enums.DataType;
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
	HandlerType getHandlerType();
	/***
	 * 得到数据类型
	 * @return
	 */
	DataType getDataType();
	/**
	 * 得到 ProtocolId
	 * @return
	 */
	int getProtocolID();
	/**
	 * 必须要已经鉴权 才能进入handler
	 * @return
	 */
	boolean needAuth();
	/***
	 * 得到requestData的class
	 */
	Class<RequestData> getRequestClass();

	/***
	 * 得到自己的RequestData
	 * @param bytes
	 * @return
	 */
	RequestData parseRequestData(byte[] bytes);
}
