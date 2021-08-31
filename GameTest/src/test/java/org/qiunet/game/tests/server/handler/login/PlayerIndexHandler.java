package org.qiunet.game.tests.server.handler.login;

import com.google.common.collect.Lists;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.game.tests.protocol.proto.Item;
import org.qiunet.game.tests.protocol.proto.PlayerIndexRequest;
import org.qiunet.game.tests.protocol.proto.PlayerIndexResponse;
import org.qiunet.game.tests.server.context.PlayerActor;
import org.qiunet.game.tests.server.handler.base.GameHandler;

/***
 * 玩家首页 handler
 *
 * qiunet
 * 2021/8/20 09:52
 **/
public class PlayerIndexHandler extends GameHandler<PlayerIndexRequest> {
	@Override
	public void handler(PlayerActor playerActor, IPersistConnRequest<PlayerIndexRequest> context) throws Exception {

		playerActor.sendMessage(PlayerIndexResponse.valueOf(Lists.newArrayList(Item.valueOf(1000, 1), Item.valueOf(1002, 2))));
	}
}
