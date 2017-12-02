package org.qiunet.flash.handler.handler.websocket;

import org.qiunet.flash.handler.common.enums.DataType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by qiunet.
 * 17/7/21
 */
public abstract class WebSocketProtobufHandler<RequestData> extends BaseWebSocketHandler<RequestData> {
	private Method method;
	@Override
	public RequestData parseRequestData(byte[] bytes) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		if (method == null ) method = getRequestClass().getMethod("parseFrom", byte[].class);
		return (RequestData) method.invoke(null, bytes);
	}

	@Override
	public DataType getDataType() {
		return DataType.PROTOBUF;
	}
}
