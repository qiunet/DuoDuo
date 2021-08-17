package org.qiunet.game.tests.client.data.condition;

import org.apache.commons.lang.math.RandomUtils;
import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.function.condition.ConditionConfig;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.tests.client.data.BlackBoard;
import org.qiunet.game.tests.client.data.condition.base.BaseRobotCondition;
import org.qiunet.game.tests.client.data.condition.base.ConditionType;

/***
 * 注册数条件
 *
 * qiunet
 * 2021/8/11 09:24
 **/
public class RegisterCountCondition extends BaseRobotCondition {
	private int maxCount = 3;

	public RegisterCountCondition() {
	}

	public RegisterCountCondition(int maxCount) {
		this.maxCount = maxCount;
	}

	@Override
	public ConditionType getType() {
		return ConditionType.REGISTER_COUNT;
	}

	@Override
	public void init(ConditionConfig config) {
		maxCount = config.getInt("maxCount", 3);
	}

	@Override
	public StatusResult verify(Robot robot) {
		// 大概率不会新创角色.
		if (RandomUtils.nextInt(100) < 99) {
			return StatusResult.FAIL;
		}
		// 没有注册信息
		if (robot.getArgument(BlackBoard.loginInfo).isEmpty()) {
			return StatusResult.FAIL;
		}
		return BlackBoard.loginInfo.get(robot).size() < maxCount ?
				StatusResult.SUCCESS : StatusResult.FAIL;
	}
}
