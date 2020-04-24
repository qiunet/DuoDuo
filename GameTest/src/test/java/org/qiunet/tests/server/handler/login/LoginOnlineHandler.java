package org.qiunet.tests.server.handler.login;

import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.websocket.IWebSocketRequest;
import org.qiunet.flash.handler.handler.websocket.WebSocketProtobufHandler;
import org.qiunet.tests.proto.LoginOnlineProto;
import org.qiunet.tests.server.startup.context.PlayerActor;

/**
 * Created by qiunet.
 * 17/12/8
 */
@RequestHandler(ID = 1001, desc = "登录Online服务")
public class LoginOnlineHandler extends WebSocketProtobufHandler<PlayerActor, LoginOnlineProto.LoginOnlineRequest> {

	@Override
	public boolean needAuth() {
		return true;
	}

	@Override
	public void handler(PlayerActor playerActor, IWebSocketRequest<LoginOnlineProto.LoginOnlineRequest> context) throws Exception {
		logger.info("LoginOnlineHandler: "+ context.getRequestData());

		LoginOnlineProto.LoginOnlineResponse response = LoginOnlineProto.LoginOnlineResponse.newBuilder()
			.setDay(context.getRequestData().getHeader().getUid())
			.build();
		playerActor.sendResponse(1000000, response);
	}
}