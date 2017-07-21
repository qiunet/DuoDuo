package org.qiunet.flash.handler.handler.proto;

import org.qiunet.flash.handler.handler.BaseHttpHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by qiunet.
 * 17/7/21
 */
public abstract class BaseProtoHttpHandler<RequestData, ResponseData> extends BaseHttpHandler<RequestData, ResponseData> {
	private Method method;
	@Override
	public RequestData parseRequestData(byte[] bytes) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		if (method == null) method = requestDataClass.getMethod("parseFrom", byte[].class);
		return (RequestData) method.invoke(null, bytes);
	}
}
