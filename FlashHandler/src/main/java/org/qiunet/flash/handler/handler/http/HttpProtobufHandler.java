package org.qiunet.flash.handler.handler.http;

import org.qiunet.flash.handler.common.enums.DataType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by qiunet.
 * 17/7/21
 */
public abstract class HttpProtobufHandler<RequestData, ResponseData> extends BaseHttpHandler<RequestData, ResponseData> {
	private Method method;
	@Override
	public RequestData parseRequestData(byte[] bytes) {
		if (method == null) try {
			method = getRequestClass().getMethod("parseFrom", byte[].class);
			return (RequestData) method.invoke(null, bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public DataType getDataType() {
		return DataType.PROTOBUF;
	}
}
