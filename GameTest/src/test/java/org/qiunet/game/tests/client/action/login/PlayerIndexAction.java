package org.qiunet.game.tests.client.action.login;

import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.game.test.response.TestResponse;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.tests.client.action.base.TestAction;
import org.qiunet.game.tests.client.data.BlackBoard;
import org.qiunet.game.tests.client.data.condition.AuthCondition;
import org.qiunet.game.tests.protocol.ProtocolId;
import org.qiunet.game.tests.protocol.proto.PlayerIndexRequest;
import org.qiunet.game.tests.protocol.proto.PlayerIndexResponse;

/***
 *
 *
 * qiunet
 * 2021/8/16 21:07
 **/
public class PlayerIndexAction extends TestAction {

	public PlayerIndexAction(Robot robot) {
		super(robot, new AuthCondition());
	}

	@Override
	protected ActionStatus execute() {
		this.sendMessage(PlayerIndexRequest.valueOf(robot.getId()));
		return ActionStatus.RUNNING;
	}

	@Override
	protected ActionStatus runningStatusUpdate() {
		return BlackBoard.items.isNull(robot) ? ActionStatus.RUNNING : ActionStatus.SUCCESS;
	}

	@TestResponse(ProtocolId.Login.PLAYER_INDEX_RSP)
	protected void indexResp(PlayerIndexResponse response) {
		BlackBoard.items.set(robot, response.getItems());
	}
}
