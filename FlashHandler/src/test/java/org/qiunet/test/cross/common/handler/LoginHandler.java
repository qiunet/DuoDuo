package org.qiunet.test.cross.common.handler;

import org.qiunet.cross.node.ServerNodeManager;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.test.cross.common.Constants;
import org.qiunet.test.cross.common.event.CrossNodeEvent;
import org.qiunet.test.cross.common.event.PlayerLoginEventData;
import org.qiunet.test.cross.common.proto.req.LoginRequest;

/***
 *
 *
 * @author qiunet
 * 2020-10-23 09:54
 */
public class LoginHandler extends BaseHandler<LoginRequest> {
	@Override
	public void handler(PlayerActor playerActor, IPersistConnRequest<LoginRequest> context) throws Exception {
		playerActor.auth(context.getRequestData().getPlayerId());
		playerActor.loginSuccess();

		playerActor.fireEvent(new PlayerLoginEventData());

		ServerNodeManager.getNode(Constants.CROSS_SERVER_ID).fireCrossEvent(CrossNodeEvent.valueOf(playerActor.getPlayerId()));
	}

	@Override
	public boolean needAuth() {
		return false;
	}
}
