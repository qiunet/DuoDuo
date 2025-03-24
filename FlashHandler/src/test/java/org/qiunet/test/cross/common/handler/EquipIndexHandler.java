package org.qiunet.test.cross.common.handler;

import org.qiunet.cross.actor.CrossPlayerActor;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.test.cross.common.Constants;
import org.qiunet.test.cross.common.event.CrossPlayerLoginEvent;
import org.qiunet.test.cross.common.proto.req.EquipIndexRequest;
import org.qiunet.test.cross.common.proto.resp.CrossLoginResponse;
import org.qiunet.utils.exceptions.CustomException;

import java.util.concurrent.atomic.AtomicInteger;

/***
 *
 *
 * @author qiunet
 * 2020-10-26 15:56
 */
public class EquipIndexHandler extends BaseTransmitHandler<EquipIndexRequest> {
	private static final AtomicInteger counter = new AtomicInteger();
	@Override
	public void handler(PlayerActor playerActor, IPersistConnRequest<EquipIndexRequest> context) throws Exception {
		if (playerActor.isCrossStatus()) {
			throw new CustomException("{} 已经跨服. 需要去crossHandler", playerActor.toString());
		}
		playerActor.crossToServer(Constants.CROSS_SERVER_ID, playerActor.msgExecuteIndex(), result -> {
			if (result) {
				playerActor.fireCrossEvent(new CrossPlayerLoginEvent());
			}
		});
	}

	@Override
	public void crossHandler(CrossPlayerActor actor, EquipIndexRequest equipIndexRequest) {
		logger.info("Cross服: 第{}次EquipIndexRequest!", counter.incrementAndGet());
		actor.sendMessage(CrossLoginResponse.valueOf("qiunet"), true);
	}
}
