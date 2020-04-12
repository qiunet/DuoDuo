package org.qiunet.flash.handler.handler.websocket;

import com.google.protobuf.GeneratedMessageV3;
import org.qiunet.flash.handler.common.enums.DataType;
import org.qiunet.flash.handler.common.player.IPlayerActor;

/**
 * Created by qiunet.
 * 17/7/21
 */
public abstract class WebSocketProtobufHandler<P extends IPlayerActor, RequestData extends GeneratedMessageV3> extends BaseWebSocketHandler<P, RequestData> {
	@Override
	public RequestData parseRequestData(byte[] bytes) {
		return getDataType().parseBytes(bytes, getRequestClass());
	}

	@Override
	public DataType getDataType() {
		return DataType.PROTOBUF;
	}
}
