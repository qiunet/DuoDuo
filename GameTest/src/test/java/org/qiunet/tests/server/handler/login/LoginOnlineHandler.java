package org.qiunet.tests.server.handler.login;

import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.websocket.IWebSocketRequest;
import org.qiunet.flash.handler.handler.websocket.WebSocketProtobufHandler;
import org.qiunet.tests.proto.LoginOnlineRequest;
import org.qiunet.tests.proto.LoginOnlineResponse;
import org.qiunet.tests.server.startup.context.PlayerActor;

/**
 * Created by qiunet.
 * 17/12/8
 */
@RequestHandler(ID = 1001, desc = "登录Online服务")
public class LoginOnlineHandler extends WebSocketProtobufHandler<PlayerActor, LoginOnlineRequest> {

	@Override
	public boolean needAuth() {
		return true;
	}

	@Override
	public void handler(PlayerActor playerActor, IWebSocketRequest<LoginOnlineRequest> context) throws Exception {
		logger.info("LoginOnlineHandler: "+ context.getRequestData());

		playerActor.sendResponse(LoginOnlineResponse.valueOf(10000));
	}
}
