package org.qiunet.game.tests.server.handler.login;

import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.flash.handler.context.status.StatusResultException;
import org.qiunet.game.tests.protocol.proto.login.LoginInfo;
import org.qiunet.game.tests.protocol.proto.login.RegisterRequest;
import org.qiunet.game.tests.protocol.proto.login.RegisterResponse;
import org.qiunet.game.tests.server.context.PlayerActor;
import org.qiunet.game.tests.server.data.ServerData;
import org.qiunet.game.tests.server.enums.GameStatus;
import org.qiunet.game.tests.server.handler.base.GameHandler;
import org.qiunet.utils.string.StringUtil;

import java.util.List;

/***
 * 注册 handler
 *
 * qiunet
 * 2021/8/20 09:52
 **/
public class RegisterHandler extends GameHandler<RegisterRequest> {
	@Override
	public void handler(PlayerActor playerActor, IPersistConnRequest<RegisterRequest> context) throws Exception {
		RegisterRequest request = context.getRequestData();
		if (StringUtil.isEmpty(playerActor.getOpenId())) {
			throw StatusResultException.valueOf(StatusResult.FAIL);
		}

		List<LoginInfo> loginInfos = ServerData.loginInfo.get(playerActor.getOpenId());
		if (loginInfos.size() > 3) {
			throw StatusResultException.valueOf(GameStatus.REGISTER_COUNT_MAX);
		}

		LoginInfo loginInfo = LoginInfo.valueOf(ServerData.playerIdGenerator.makeId(), request.getGender(), request.getNick(), 1, request.getImg());
		playerActor.sendMessage(RegisterResponse.valueOf(loginInfo));
	}

	@Override
	public boolean needAuth() {
		return false;
	}
}
