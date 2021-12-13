package org.qiunet.game.tests.client.action.login;

import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.ai.node.action.BehaviorAction;
import org.qiunet.function.condition.ConditionManager;
import org.qiunet.function.condition.IConditions;
import org.qiunet.game.test.response.TestResponse;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.tests.client.action.base.TestAction;
import org.qiunet.game.tests.client.data.BlackBoard;
import org.qiunet.game.tests.protocol.ProtocolId;
import org.qiunet.game.tests.protocol.proto.login.RandomNameRequest;
import org.qiunet.game.tests.protocol.proto.login.RandomNameResponse;

/***
 * 随机名称获取
 *
 * qiunet
 * 2021/8/8 10:08
 **/
@BehaviorAction(desc = "随机名称")
public class RandomNameAction extends TestAction {

	public RandomNameAction(Robot robot) {
		this(robot, (IConditions<Robot>) ConditionManager.EMPTY_CONDITION);
	}
	public RandomNameAction(Robot robot, IConditions<Robot> conditions) {
		super(robot, conditions);
	}

	@Override
	public ActionStatus execute() {
		this.sendMessage(RandomNameRequest.valueOf());
		return ActionStatus.RUNNING;
	}

	@Override
	protected ActionStatus runningStatusUpdate() {
		return BlackBoard.randomName.isNull(robot) ? ActionStatus.RUNNING : ActionStatus.SUCCESS;
	}

	@TestResponse(ProtocolId.Login.RANDOM_NAME_RSP)
	public void randomNameRsp(RandomNameResponse resp) {
		BlackBoard.randomName.set(robot, resp.getNick());
	}
}
