package org.qiunet.game.tests.server.handler.player;

import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.game.tests.protocol.proto.login.PlayerData;
import org.qiunet.game.tests.protocol.proto.player.UpgradeLevelRequest;
import org.qiunet.game.tests.protocol.proto.player.UpgradeLevelResponse;
import org.qiunet.game.tests.server.context.PlayerActor;
import org.qiunet.game.tests.server.handler.base.GameHandler;

/***
 *
 *
 * qiunet
 * 2021/9/2 16:09
 **/
public class UpgradeLvHandler extends GameHandler<UpgradeLevelRequest> {
	@Override
	public void handler(PlayerActor playerActor, IPersistConnRequest<UpgradeLevelRequest> context) throws Exception {
		PlayerData playerData = playerActor.getPlayerData();
		playerData.upgrade();

		playerActor.sendMessage(UpgradeLevelResponse.valueOf(playerData.getLv()));
	}
}
