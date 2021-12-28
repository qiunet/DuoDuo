package org.qiunet.test.cross.common.handler;

import org.qiunet.cross.actor.CrossPlayerActor;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.test.cross.common.Constants;
import org.qiunet.test.cross.common.data.PlayerCrossData;
import org.qiunet.test.cross.common.data.TestCrossDataUser;
import org.qiunet.test.cross.common.event.CrossPlayerLoginEventData;
import org.qiunet.test.cross.common.proto.req.EquipIndexRequest;
import org.qiunet.test.cross.common.proto.resp.CrossLoginResponse;

/***
 *
 *
 * @author qiunet
 * 2020-10-26 15:56
 */
public class EquipIndexHandler extends BaseTransmitHandler<EquipIndexRequest> {

	@Override
	public void crossHandler(CrossPlayerActor actor, EquipIndexRequest equipIndexRequest) {
		TestCrossDataUser crossData = actor.getCrossData(PlayerCrossData.TEST_CROSS_DATA);
		logger.info("取到CrossData: PlayerId: {} playerName: {}", crossData.getUid(), crossData.getPlayerName());
		actor.sendMessage(CrossLoginResponse.valueOf("qiunet"));
	}

	@Override
	public void handler(PlayerActor playerActor, IPersistConnRequest<EquipIndexRequest> context) throws Exception {
		playerActor.crossToServer(Constants.CROSS_SERVER_ID);
		playerActor.fireCrossEvent(new CrossPlayerLoginEventData());
	}
}
