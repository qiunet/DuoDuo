package org.qiunet.game.tests.client.data.condition;

import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.tests.client.data.BlackBoard;
import org.qiunet.game.tests.client.data.condition.base.BaseRobotCondition;
import org.qiunet.game.tests.protocol.proto.login.PlayerData;

/***
 *
 *
 * qiunet
 * 2021/9/2 10:48
 **/
public class ExpEnoughCondition extends BaseRobotCondition {

	@Override
	public StatusResult verify(Robot robot) {
		//为测试方便 默认100 经验就可以升级. 并且没有等级上限.
		return robot.getVal(BlackBoard.playerData).getExp() >= PlayerData.DEFAULT_UPGRADE_EXP ? StatusResult.SUCCESS : StatusResult.FAIL;
	}
}
