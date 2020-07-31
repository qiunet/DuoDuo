package org.qiunet.flash.handler.handler.http;

import org.qiunet.flash.handler.common.enums.DataType;

/**
 * Created by qiunet.
 * 17/11/21
 */
public abstract class HttpStringHandler extends BaseHttpHandler<String, String> {

	@Override
	public DataType getDataType() {
		return DataType.STRING;
	}

	@Override
	public String parseRequestData(byte[] bytes) {
		return getDataType().parseBytes(bytes);
	}
}
