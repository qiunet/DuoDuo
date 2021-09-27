package org.qiunet.game.tests.client.data.condition;

import org.apache.commons.lang.math.RandomUtils;
import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.test.robot.condition.RobotCondition;
import org.qiunet.game.tests.client.data.BlackBoard;

/***
 * 注册数条件
 *
 * qiunet
 * 2021/8/11 09:24
 **/
public class RegisterCountCondition extends RobotCondition {
	private int maxCount = 3;

	public RegisterCountCondition() {
	}

	public RegisterCountCondition(int maxCount) {
		this.maxCount = maxCount;
	}

	@Override
	public StatusResult verify(Robot robot) {
		// 没有注册信息
		if (robot.getArgument(BlackBoard.loginInfo).isNull()) {
			return StatusResult.FAIL;
		}
		// 没有角色. 可以注册角色
		if (BlackBoard.loginInfo.get(robot).isEmpty()) {
			return StatusResult.SUCCESS;
		}

		// 大概率不会新创角色.
		if (RandomUtils.nextInt(100) < 99) {
			return StatusResult.FAIL;
		}
		return BlackBoard.loginInfo.get(robot).size() < maxCount ?
				StatusResult.SUCCESS : StatusResult.FAIL;
	}
}
