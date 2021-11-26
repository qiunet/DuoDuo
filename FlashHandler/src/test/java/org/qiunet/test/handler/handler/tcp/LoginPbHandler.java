package org.qiunet.test.handler.handler.tcp;

import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;
import org.qiunet.test.handler.proto.LoginResponse;
import org.qiunet.test.handler.proto.TcpPbLoginRequest;

/**
 * Created by qiunet.
 * 17/8/16
 */

public class LoginPbHandler extends PersistConnPbHandler<PlayerActor, TcpPbLoginRequest> {

	@Override
	public void handler(PlayerActor player, IPersistConnRequest<TcpPbLoginRequest> context)throws Exception {
		player.sendMessage(LoginResponse.valueOf(context.getRequestData().getAccount()));
	}

	@Override
	public boolean needAuth() {
		return false;
	}
}
