package org.qiunet.cross.test.handler;

import org.qiunet.cross.actor.CrossPlayerActor;
import org.qiunet.cross.test.common.Constants;
import org.qiunet.cross.test.common.actor.PlayerActor;
import org.qiunet.cross.test.event.CrossPlayerLoginEventData;
import org.qiunet.cross.test.proto.req.EquipIndexRequest;
import org.qiunet.cross.test.proto.resp.CrossLoginResponse;
import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.websocket.IWebSocketRequest;

/***
 *
 *
 * @author qiunet
 * 2020-10-26 15:56
 */
@RequestHandler(ID = ProtocolId.Equip.EQUIP_INDEX, desc = "装备首页")
public class EquipIndexHandler extends BaseTransmitHandler<EquipIndexRequest> {
	@Override
	public void handler(PlayerActor playerActor, IWebSocketRequest<EquipIndexRequest> context) throws Exception {
		playerActor.cross(Constants.CROSS_SERVER_ID);
		playerActor.fireCrossEvent(new CrossPlayerLoginEventData());
	}

	@Override
	public void crossHandler(CrossPlayerActor actor, EquipIndexRequest equipIndexRequest) {
		actor.sendMessage(CrossLoginResponse.valueOf("qiunet"));
	}
}
