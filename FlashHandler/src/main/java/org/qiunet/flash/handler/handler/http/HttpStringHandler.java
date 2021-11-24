package org.qiunet.flash.handler.handler.http;

import org.qiunet.flash.handler.common.enums.DataType;
import org.qiunet.flash.handler.handler.BaseHandler;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Created by qiunet.
 * 17/11/21
 */
public abstract class HttpStringHandler extends BaseHandler<String> {

	@Override
	public DataType getDataType() {
		return DataType.STRING;
	}

	@Override
	public String parseRequestData(ByteBuffer buffer) {
		return StandardCharsets.UTF_8.decode(buffer).toString();
	}
}
