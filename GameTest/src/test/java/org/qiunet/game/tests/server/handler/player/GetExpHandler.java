package org.qiunet.game.tests.server.handler.player;

import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.game.tests.client.data.BlackBoard;
import org.qiunet.game.tests.protocol.proto.login.PlayerData;
import org.qiunet.game.tests.protocol.proto.player.GetExpRequest;
import org.qiunet.game.tests.protocol.proto.player.GetExpResponse;
import org.qiunet.game.tests.server.handler.base.GameHandler;
import org.qiunet.utils.math.MathUtil;

/***
 *
 *
 * qiunet
 * 2021/9/2 15:30
 **/
public class GetExpHandler extends GameHandler<GetExpRequest> {
	@Override
	public void handler(PlayerActor playerActor, IPersistConnRequest<GetExpRequest> context) throws Exception {
		int addExp = MathUtil.random(10, 60);

		playerActor.computeIfAbsent(BlackBoard.playerData, () -> PlayerData.valueOf(playerActor)).addExp(addExp);
		playerActor.sendMessage(GetExpResponse.valueOf(addExp));
	}
}
