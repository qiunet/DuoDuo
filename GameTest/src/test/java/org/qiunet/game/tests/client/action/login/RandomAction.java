package org.qiunet.game.tests.client.action.login;

import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.game.test.response.TestResponse;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.tests.client.action.base.TestAction;
import org.qiunet.game.tests.client.data.BlackBoard;
import org.qiunet.game.tests.client.data.condition.RegisterCountCondition;
import org.qiunet.game.tests.protocol.ProtocolId;
import org.qiunet.game.tests.protocol.proto.RandomNameRequest;
import org.qiunet.game.tests.protocol.proto.RandomNameResponse;

/***
 * 随机名称获取
 *
 * qiunet
 * 2021/8/8 10:08
 **/
public class RandomAction extends TestAction {

	public RandomAction(Robot robot) {
		super(robot, new RegisterCountCondition(3));
	}

	@Override
	public ActionStatus execute() {
		this.sendMessage(RandomNameRequest.valueOf());
		return ActionStatus.SUCCESS;
	}

	@TestResponse(ProtocolId.Login.RANDOM_NAME_RSP)
	public void randomNameRsp(RandomNameResponse resp) {
		BlackBoard.randomName.set(robot, resp.getNick());
	}
}
