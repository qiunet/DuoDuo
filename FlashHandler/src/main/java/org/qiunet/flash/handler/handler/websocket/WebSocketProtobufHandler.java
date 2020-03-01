package org.qiunet.flash.handler.handler.websocket;

import com.google.protobuf.GeneratedMessageV3;
import org.qiunet.flash.handler.common.enums.DataType;

/**
 * Created by qiunet.
 * 17/7/21
 */
public abstract class WebSocketProtobufHandler<RequestData extends GeneratedMessageV3> extends BaseWebSocketHandler<RequestData> {
	@Override
	public RequestData parseRequestData(byte[] bytes) {
		return getDataType().parseBytes(bytes, getRequestClass());
	}

	@Override
	public DataType getDataType() {
		return DataType.PROTOBUF;
	}
}
