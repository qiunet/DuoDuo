package org.qiunet.flash.handler.handler.websocket;

import org.qiunet.flash.handler.common.enums.DataType;

/**
 * Created by qiunet.
 * 17/11/21
 */
public abstract class WebSocketStringHandler extends BaseWebSocketHandler<String> {

	@Override
	public DataType getDataType() {
		return DataType.STRING;
	}

	@Override
	public String parseRequestData(byte[] bytes) {
		return getDataType().parseBytes(bytes);
	}
}
