package org.qiunet.flash.handler.handler.http;

import org.qiunet.flash.handler.common.enums.DataType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by qiunet.
 * 17/7/21
 */
public abstract class HttpProtobufHandler<RequestData, ResponseData> extends BaseHttpHandler<RequestData, ResponseData> {
	@Override
	public RequestData parseRequestData(byte[] bytes) {
		return getDataType().parseBytes(bytes, getRequestClass());
	}

	@Override
	public DataType getDataType() {
		return DataType.PROTOBUF;
	}
}
