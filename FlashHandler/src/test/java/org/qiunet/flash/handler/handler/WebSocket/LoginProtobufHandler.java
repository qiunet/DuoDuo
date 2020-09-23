package org.qiunet.flash.handler.handler.WebSocket;

import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.websocket.IWebSocketRequest;
import org.qiunet.flash.handler.handler.websocket.WebSocketProtobufHandler;
import org.qiunet.flash.handler.proto.LoginRequest;
import org.qiunet.flash.handler.proto.LoginResponse;
import org.qiunet.flash.handler.startup.context.PlayerActor;

/**
 * Created by qiunet.
 * 17/8/16
 */
@RequestHandler(ID = 1006, desc = "protobuf 测试")
public class LoginProtobufHandler extends WebSocketProtobufHandler<PlayerActor, LoginRequest> {

	@Override
	public void handler(PlayerActor playerActor, IWebSocketRequest<LoginRequest> context) throws Exception {
		playerActor.sendResponse(LoginResponse.valueOf(context.getRequestData().getAccount()));
	}
}
