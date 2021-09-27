package org.qiunet.game.tests.client.data.condition;

import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.test.robot.condition.RobotCondition;
import org.qiunet.game.tests.client.data.BlackBoard;

/***
 *
 *
 * qiunet
 * 2021/8/11 16:25
 **/
public class LoginCondition extends RobotCondition {

	@Override
	public StatusResult verify(Robot robot) {
		return (! BlackBoard.loginInfo.isNull(robot)) ? StatusResult.SUCCESS : StatusResult.FAIL;
	}
}
