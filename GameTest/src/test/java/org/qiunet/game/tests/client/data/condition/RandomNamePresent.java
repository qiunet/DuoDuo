package org.qiunet.game.tests.client.data.condition;

import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.function.condition.ConditionConfig;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.tests.client.data.BlackBoard;
import org.qiunet.game.tests.client.data.condition.base.BaseRobotCondition;
import org.qiunet.game.tests.client.data.condition.base.ConditionType;
import org.qiunet.utils.string.StringUtil;

/***
 * 随机名称是否已经获取到
 *
 * qiunet
 * 2021/8/11 17:27
 **/
public class RandomNamePresent extends BaseRobotCondition {
	@Override
	public ConditionType getType() {
		return ConditionType.RANDOM_NAME_PRESENT;
	}

	@Override
	public void init(ConditionConfig config) {

	}

	@Override
	public StatusResult verify(Robot robot) {
		return !StringUtil.isEmpty(BlackBoard.randomName.get(robot)) ? StatusResult.SUCCESS : StatusResult.FAIL;
	}
}
