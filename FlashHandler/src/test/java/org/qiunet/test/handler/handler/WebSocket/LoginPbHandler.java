package org.qiunet.test.handler.handler.WebSocket;

import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;
import org.qiunet.test.handler.proto.LoginResponse;
import org.qiunet.test.handler.proto.WsPbLoginRequest;

/**
 * Created by qiunet.
 * 17/8/16
 */

public class LoginPbHandler extends PersistConnPbHandler<PlayerActor, WsPbLoginRequest> {

	@Override
	public void handler(PlayerActor playerActor, IPersistConnRequest<WsPbLoginRequest> context) throws Exception {
		playerActor.sendMessage(LoginResponse.valueOf(context.getRequestData().getAccount()));
	}

	@Override
	public boolean needAuth() {
		return false;
	}
}
