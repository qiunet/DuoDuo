package org.qiunet.flash.handler.handler.WebSocket;

import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.websocket.IWebSocketRequest;
import org.qiunet.flash.handler.context.response.push.DefaultStringMessage;
import org.qiunet.flash.handler.handler.websocket.WebSocketStringHandler;
import org.qiunet.flash.handler.startup.context.PlayerActor;

/**
 * Created by qiunet.
 * 17/11/21
 */
@RequestHandler(ID = 1005, desc = "测试")
public class LoginHandler extends WebSocketStringHandler<PlayerActor> {

	@Override
	public void handler(PlayerActor playerActor, IWebSocketRequest<String> context) throws Exception {
		playerActor.send(new DefaultStringMessage(2000, context.getRequestData()));
	}
}
