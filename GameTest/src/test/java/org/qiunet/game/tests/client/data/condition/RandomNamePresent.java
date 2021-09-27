package org.qiunet.game.tests.client.data.condition;

import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.test.robot.condition.RobotCondition;
import org.qiunet.game.tests.client.data.BlackBoard;
import org.qiunet.utils.string.StringUtil;

/***
 * 随机名称是否已经获取到
 *
 * qiunet
 * 2021/8/11 17:27
 **/
public class RandomNamePresent extends RobotCondition {

	@Override
	public StatusResult verify(Robot robot) {
		return !StringUtil.isEmpty(BlackBoard.randomName.get(robot)) ? StatusResult.SUCCESS : StatusResult.FAIL;
	}
}
