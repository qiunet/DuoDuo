package org.qiunet.flash.handler.handler.http;

import org.qiunet.flash.handler.common.enums.DataType;
import org.qiunet.flash.handler.common.message.MessageContent;

import java.nio.charset.StandardCharsets;

/**
 * Created by qiunet.
 * 17/11/21
 */
public abstract class HttpStringHandler extends BaseHttpHandler<String> {

	@Override
	public DataType getDataType() {
		return DataType.STRING;
	}

	@Override
	public String parseRequestData(MessageContent content) {
		return content.byteBuf().toString(StandardCharsets.UTF_8);
	}
}
