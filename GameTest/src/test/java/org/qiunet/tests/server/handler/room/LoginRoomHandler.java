package org.qiunet.tests.server.handler.room;

import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;
import org.qiunet.tests.proto.LoginRoomRequest;
import org.qiunet.tests.proto.LoginRoomResponse;
import org.qiunet.tests.server.startup.context.PlayerActor;

/**
 * Created by qiunet.
 * 17/12/9
 */

public class LoginRoomHandler extends PersistConnPbHandler<PlayerActor, LoginRoomRequest> {

	@Override
	public void handler(PlayerActor playerActor, IPersistConnRequest<LoginRoomRequest> context) throws Exception {
		logger.info("LoginRoomHandler received message "+context.getRequestData());
		playerActor.sendMessage(LoginRoomResponse.valueOf(10));
	}
}
