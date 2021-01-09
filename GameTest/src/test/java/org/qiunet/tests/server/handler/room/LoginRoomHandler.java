package org.qiunet.tests.server.handler.room;

import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;
import org.qiunet.tests.proto.LoginRoomRequest;
import org.qiunet.tests.proto.LoginRoomResponse;
import org.qiunet.tests.server.startup.context.PlayerActor;

/**
 * Created by qiunet.
 * 17/12/9
 */
@RequestHandler(ID = 1003, desc = "房间服的登录")
public class LoginRoomHandler extends PersistConnPbHandler<PlayerActor, LoginRoomRequest> {

	@Override
	public void handler(PlayerActor playerActor, IPersistConnRequest<LoginRoomRequest> context) throws Exception {
		logger.info("LoginRoomHandler received message "+context.getRequestData());
		playerActor.sendResponse(LoginRoomResponse.valueOf(10));
	}
}
