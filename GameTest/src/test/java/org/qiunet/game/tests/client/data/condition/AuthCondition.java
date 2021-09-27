package org.qiunet.game.tests.client.data.condition;

import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.test.robot.condition.RobotCondition;

/***
 * 是否已经鉴权.
 *
 * qiunet
 * 2021/8/11 17:22
 **/
public class AuthCondition extends RobotCondition {

	@Override
	public StatusResult verify(Robot robot) {
		return robot.isAuth() ? StatusResult.SUCCESS: StatusResult.FAIL;
	}
}
