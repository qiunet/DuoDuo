package org.qiunet.tests.server.handler.player;

import com.google.common.collect.Lists;
import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;
import org.qiunet.tests.proto.Item;
import org.qiunet.tests.proto.PlayerIndexRequest;
import org.qiunet.tests.proto.PlayerIndexResponse;
import org.qiunet.tests.server.startup.context.PlayerActor;

/**
 * Created by qiunet.
 * 17/12/9
 */
@RequestHandler(ID = 1002, desc = "长连接首页")
public class PlayerIndexTcpHandler extends PersistConnPbHandler<PlayerActor, PlayerIndexRequest> {

	@Override
	public void handler(PlayerActor playerActor, IPersistConnRequest<PlayerIndexRequest> context) throws Exception {
		playerActor.sendResponse(PlayerIndexResponse.valueOf(Lists.newArrayList(
			Item.valueOf(123450, 1),
			Item.valueOf(123451, 2),
			Item.valueOf(123452, 3),
			Item.valueOf(123453, 4),
			Item.valueOf(123454, 5)
		)));
	}
}
