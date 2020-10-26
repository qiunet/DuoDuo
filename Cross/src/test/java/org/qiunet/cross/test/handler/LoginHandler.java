package org.qiunet.cross.test.handler;

import org.qiunet.cross.test.common.actor.PlayerActor;
import org.qiunet.cross.test.event.PlayerLoginEventData;
import org.qiunet.cross.test.proto.req.LoginRequest;
import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.websocket.IWebSocketRequest;

/***
 *
 *
 * @author qiunet
 * 2020-10-23 09:54
 */
@RequestHandler(ID = ProtocolId.Player.PLAYER_LOGIN, desc = "登录请求")
public class LoginHandler extends BaseHandler<LoginRequest> {
	@Override
	public void handler(PlayerActor playerActor, IWebSocketRequest<LoginRequest> context) throws Exception {
		playerActor.auth(context.getRequestData().getPlayerId());

		playerActor.fireEvent(new PlayerLoginEventData());
	}

	@Override
	public boolean needAuth() {
		return false;
	}
}
