package org.qiunet.flash.handler.handler.WebSocket;

import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;
import org.qiunet.flash.handler.proto.LoginResponse;
import org.qiunet.flash.handler.proto.WsPbLoginRequest;
import org.qiunet.flash.handler.startup.context.PlayerActor;

/**
 * Created by qiunet.
 * 17/8/16
 */
@RequestHandler(ID = 1006, desc = "protobuf 测试")
public class LoginPbHandler extends PersistConnPbHandler<PlayerActor, WsPbLoginRequest> {

	@Override
	public void handler(PlayerActor playerActor, IPersistConnRequest<WsPbLoginRequest> context) throws Exception {
		playerActor.sendMessage(LoginResponse.valueOf(context.getRequestData().getAccount()));
	}
}
