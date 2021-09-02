package org.qiunet.game.tests.server.handler.login;

import com.google.common.collect.Lists;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.context.status.StatusResultException;
import org.qiunet.game.tests.protocol.proto.login.RandomNameRequest;
import org.qiunet.game.tests.protocol.proto.login.RandomNameResponse;
import org.qiunet.game.tests.server.context.PlayerActor;
import org.qiunet.game.tests.server.data.ServerData;
import org.qiunet.game.tests.server.enums.GameStatus;
import org.qiunet.game.tests.server.handler.base.GameHandler;

import java.util.HashSet;
import java.util.Set;

/***
 * 获取随机名称 handler
 *
 * qiunet
 * 2021/8/20 09:52
 **/
public class RandomNameHandler extends GameHandler<RandomNameRequest> {
	@Override
	public void handler(PlayerActor playerActor, IPersistConnRequest<RandomNameRequest> context) throws Exception {
		Set<String> set = new HashSet<>(ServerData.nameSets);
		set.removeAll(ServerData.usedNameSets);
		if (set.isEmpty()) {
			throw StatusResultException.valueOf(GameStatus.RANDOM_NAME_POOL_EMPTY);
		}
		playerActor.sendMessage(RandomNameResponse.valueOf(Lists.newArrayList(set).get(0)));
	}

	@Override
	public boolean needAuth() {
		return false;
	}
}
