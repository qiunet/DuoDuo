package org.qiunet.flash.handler.handler.WebSocket;

import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.websocket.IWebSocketRequest;
import org.qiunet.flash.handler.context.response.push.DefaultProtobufMessage;
import org.qiunet.flash.handler.context.response.push.ResponseMsgUtil;
import org.qiunet.flash.handler.handler.proto.LoginProto;
import org.qiunet.flash.handler.handler.websocket.WebSocketProtobufHandler;

/**
 * Created by qiunet.
 * 17/8/16
 */
@RequestHandler(ID = 1006, desc = "protobuf 测试")
public class LoginProtobufHandler extends WebSocketProtobufHandler<LoginProto.LoginRequest> {

	@Override
	public void handler(IWebSocketRequest<LoginProto.LoginRequest> context) {
		ResponseMsgUtil.responseWebsocketMessage(context.channel(), new DefaultProtobufMessage(2001, LoginProto.LoginResponse.newBuilder().setTestString(context.getRequestData().getTestString()).build()));
	}
}
