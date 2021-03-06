package org.qiunet.cross.test.handler;

import org.qiunet.cross.actor.CrossPlayerActor;
import org.qiunet.cross.test.common.Constants;
import org.qiunet.cross.test.common.actor.PlayerActor;
import org.qiunet.cross.test.data.PlayerCrossData;
import org.qiunet.cross.test.data.TestCrossDataCross;
import org.qiunet.cross.test.event.CrossPlayerLoginEventData;
import org.qiunet.cross.test.proto.req.EquipIndexRequest;
import org.qiunet.cross.test.proto.resp.CrossLoginResponse;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;

/***
 *
 *
 * @author qiunet
 * 2020-10-26 15:56
 */
public class EquipIndexHandler extends BaseTransmitHandler<EquipIndexRequest> {
	@Override
	public void handler(PlayerActor playerActor, IPersistConnRequest<EquipIndexRequest> context) throws Exception {
		playerActor.cross(Constants.CROSS_SERVER_ID);
		playerActor.fireCrossEvent(new CrossPlayerLoginEventData());
	}

	@Override
	public void crossHandler(CrossPlayerActor actor, EquipIndexRequest equipIndexRequest) {
		TestCrossDataCross crossData = actor.getCrossData(PlayerCrossData.TEST_CROSS_DATA);
		logger.info("取到CrossData: PlayerId: {} playerName: {}", crossData.getUid(), crossData.getPlayerName());
		actor.sendMessage(CrossLoginResponse.valueOf("qiunet"));
	}
}
