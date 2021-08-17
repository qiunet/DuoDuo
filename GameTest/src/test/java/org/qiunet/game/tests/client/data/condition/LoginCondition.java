package org.qiunet.game.tests.client.data.condition;

import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.tests.client.data.BlackBoard;
import org.qiunet.game.tests.client.data.condition.base.BaseRobotCondition;

/***
 *
 *
 * qiunet
 * 2021/8/11 16:25
 **/
public class LoginCondition extends BaseRobotCondition {

	@Override
	public StatusResult verify(Robot robot) {
		return (! BlackBoard.loginInfo.isEmpty(robot)) ? StatusResult.SUCCESS : StatusResult.FAIL;
	}
}
