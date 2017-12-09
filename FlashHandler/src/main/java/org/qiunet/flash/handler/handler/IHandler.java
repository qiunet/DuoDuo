package org.qiunet.flash.handler.handler;

import org.qiunet.flash.handler.common.enums.DataType;
import org.qiunet.flash.handler.common.enums.HandlerType;

import java.lang.reflect.InvocationTargetException;

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
	/***
	 * 得到数据类型
	 * @return
	 */
	public DataType getDataType();
	/**
	 * 得到 ProtocolId
	 * @return
	 */
	public int getProtocolID();
	/**
	 * 必须要sid 才能进入action
	 * @return
	 */
	public boolean needToken();
	/***
	 * 得到requestData的class
	 */
	public Class<RequestData> getRequestClass();

	/***
	 * 得到自己的RequestData
	 * @param bytes
	 * @return
	 */
	public RequestData parseRequestData(byte [] bytes) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
}
