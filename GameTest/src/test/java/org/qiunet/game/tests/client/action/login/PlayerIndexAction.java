package org.qiunet.game.tests.client.action.login;

import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.ai.node.action.BehaviorAction;
import org.qiunet.function.condition.IConditions;
import org.qiunet.game.test.response.TestResponse;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.tests.client.action.base.TestAction;
import org.qiunet.game.tests.client.data.BlackBoard;
import org.qiunet.game.tests.protocol.proto.login.LoginInfo;
import org.qiunet.game.tests.protocol.proto.login.PlayerIndexRequest;
import org.qiunet.game.tests.protocol.proto.login.PlayerIndexResponse;

import java.util.List;

/***
 *
 *
 * qiunet
 * 2021/8/16 21:07
 **/
@BehaviorAction(name = "进入首页")
public class PlayerIndexAction extends TestAction {

	public PlayerIndexAction(IConditions<Robot> conditions) {
		super(conditions);
	}

	@Override
	protected ActionStatus execute() {
		// 默认取最后一个角色. 客户端应该是玩家自己选择.
		List<LoginInfo> loginInfos = BlackBoard.loginInfo.get(getOwner());
		this.sendMessage(PlayerIndexRequest.valueOf(loginInfos.get(loginInfos.size() - 1).getPlayerId()));
		return ActionStatus.RUNNING;
	}

	@Override
	protected ActionStatus runningStatusUpdate() {
		return !getOwner().isAuth() ? ActionStatus.RUNNING : ActionStatus.SUCCESS;
	}

	@TestResponse
	protected void indexResp(PlayerIndexResponse response) {
		BlackBoard.playerData.set(getOwner(), response.getPlayerData());
		BlackBoard.items.set(getOwner(), response.getItems());
		getOwner().setId(response.getPlayerData().getPlayerId());
	}
}
