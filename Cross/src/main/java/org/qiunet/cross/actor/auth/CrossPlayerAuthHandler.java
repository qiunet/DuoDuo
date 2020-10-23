package org.qiunet.cross.actor.auth;

import org.qiunet.cross.actor.CrossPlayerActor;
import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequest;
import org.qiunet.flash.handler.handler.tcp.TcpProtobufHandler;

/***
 *
 *
 * @author qiunet
 * 2020-10-23 16:50
 */
@RequestHandler(ID = IProtocolId.System.CROSS_PLAYER_AUTH, desc = "跨服鉴权")
public class CrossPlayerAuthHandler extends TcpProtobufHandler<CrossPlayerActor, CrossPlayerAuthRequest> {
	@Override
	public void handler(CrossPlayerActor playerActor, ITcpRequest<CrossPlayerAuthRequest> context) throws Exception {
		playerActor.auth(context.getRequestData().getPlayerId());
	}

	@Override
	public boolean needAuth() {
		return false;
	}
}
