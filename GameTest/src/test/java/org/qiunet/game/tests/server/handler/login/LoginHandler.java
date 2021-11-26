package org.qiunet.game.tests.server.handler.login;

import com.google.common.collect.Lists;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.game.tests.protocol.proto.login.LoginInfo;
import org.qiunet.game.tests.protocol.proto.login.LoginRequest;
import org.qiunet.game.tests.protocol.proto.login.LoginResponse;
import org.qiunet.game.tests.server.data.ServerData;
import org.qiunet.game.tests.server.handler.base.GameHandler;

import java.util.List;

/***
 * 登录handler
 *
 * qiunet
 * 2021/8/20 09:52
 **/
public class LoginHandler extends GameHandler<LoginRequest> {

	@Override
	public void handler(PlayerActor playerActor, IPersistConnRequest<LoginRequest> context) throws Exception {
		String openId = context.getRequestData().getOpenId();
		// 正常逻辑这里需要 判断openId 是否合法.
		playerActor.auth(10000);
		playerActor.setOpenId(openId);
		List<LoginInfo> loginInfos = ServerData.loginInfo.computeIfAbsent(openId, key -> Lists.newArrayListWithExpectedSize(3));
		playerActor.sendMessage(LoginResponse.valueOf(loginInfos));
	}

	@Override
	public boolean needAuth() {
		return false;
	}
}
