package org.qiunet.game.tests.client.action.login;

import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.game.test.response.TestResponse;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.tests.client.action.base.TestAction;
import org.qiunet.game.tests.client.data.BlackBoard;
import org.qiunet.game.tests.client.data.condition.RoleCountCondition;
import org.qiunet.game.tests.protocol.ProtocolId;
import org.qiunet.game.tests.protocol.proto.LoginInfo;
import org.qiunet.game.tests.protocol.proto.PlayerIndexRequest;
import org.qiunet.game.tests.protocol.proto.PlayerIndexResponse;

import java.util.List;

/***
 *
 *
 * qiunet
 * 2021/8/16 21:07
 **/
public class PlayerIndexAction extends TestAction {

	public PlayerIndexAction(Robot robot) {
		super(robot, new RoleCountCondition(1));
	}

	@Override
	protected ActionStatus execute() {
		// 默认取最后一个角色. 客户端应该是玩家自己选择.
		List<LoginInfo> loginInfos = BlackBoard.loginInfo.get(robot);
		this.sendMessage(PlayerIndexRequest.valueOf(loginInfos.get(loginInfos.size() - 1).getPlayerId()));
		return ActionStatus.RUNNING;
	}

	@Override
	protected ActionStatus runningStatusUpdate() {
		return !robot.isAuth() ? ActionStatus.RUNNING : ActionStatus.SUCCESS;
	}

	@TestResponse(ProtocolId.Login.PLAYER_INDEX_RSP)
	protected void indexResp(PlayerIndexResponse response) {
		BlackBoard.items.set(robot, response.getItems());
	}
}
