package org.qiunet.flash.handler.handler.http;

import org.qiunet.flash.handler.common.enums.DataType;
import org.qiunet.flash.handler.handler.BaseHandler;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.reflect.ReflectUtil;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * json的方式 http
 * Created by qiunet.
 * 18/1/29
 */

public abstract class HttpJsonHandler<RequestData> extends BaseHandler<RequestData> {

	public HttpJsonHandler() {
		super();
		Class<?> type = ReflectUtil.findGenericParameterizedType(this.getClass(), (c1, c2) -> HttpJsonHandler.class.isAssignableFrom(c1));
		if (type == null) {
			throw new IllegalArgumentException("HttpJsonHandler No generator type!");
		}
		ReflectUtil.setField(this, "requestDataClass", type);
	}

	@Override
	public DataType getDataType() {
		return DataType.JSON;
	}

	@Override
	public RequestData parseRequestData(ByteBuffer buffer) {
		String json = StandardCharsets.UTF_8.decode(buffer).toString();
		return JsonUtil.getGeneralObj(json, getRequestClass());
	}
}
