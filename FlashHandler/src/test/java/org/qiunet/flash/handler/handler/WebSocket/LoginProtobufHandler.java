package org.qiunet.flash.handler.handler.WebSocket;

import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequest;
import org.qiunet.flash.handler.context.request.websocket.IWebSocketRequest;
import org.qiunet.flash.handler.handler.proto.LoginProto;
import org.qiunet.flash.handler.handler.tcp.TcpProtobufHandler;
import org.qiunet.flash.handler.handler.websocket.WebSocketProtobufHandler;

/**
 * Created by qiunet.
 * 17/8/16
 */
@RequestHandler(ID = 1006, desc = "protobuf 测试")
public class LoginProtobufHandler extends WebSocketProtobufHandler<LoginProto.LoginRequest> {

	@Override
	public void handler(IWebSocketRequest<LoginProto.LoginRequest> context) {
		context.response(2001, LoginProto.LoginResponse.newBuilder().setTestString(context.getRequestData().getTestString()).build());
	}
}
