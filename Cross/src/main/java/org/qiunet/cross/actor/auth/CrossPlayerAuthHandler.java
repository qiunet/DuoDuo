package org.qiunet.cross.actor.auth;

import org.qiunet.cross.actor.CrossPlayerActor;
import org.qiunet.cross.actor.event.CrossPlayerAuthSuccessEventData;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;

/***
 *
 *
 * @author qiunet
 * 2020-10-23 16:50
 */
public class CrossPlayerAuthHandler extends PersistConnPbHandler<CrossPlayerActor, CrossPlayerAuthRequest> {
	@Override
	public void handler(CrossPlayerActor playerActor, IPersistConnRequest<CrossPlayerAuthRequest> context) throws Exception {
		playerActor.setServerId(context.getRequestData().getServerId());
		playerActor.auth(context.getRequestData().getPlayerId());

		playerActor.fireEvent(CrossPlayerAuthSuccessEventData.valueOf());
	}

	@Override
	public boolean needAuth() {
		return false;
	}
}
