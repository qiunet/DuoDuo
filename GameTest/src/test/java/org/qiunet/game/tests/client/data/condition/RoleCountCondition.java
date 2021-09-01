package org.qiunet.game.tests.client.data.condition;

import com.google.common.base.Preconditions;
import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.tests.client.data.BlackBoard;
import org.qiunet.game.tests.client.data.condition.base.BaseRobotCondition;

/***
 *
 *
 * qiunet
 * 2021/9/1 17:55
 **/
public class RoleCountCondition extends BaseRobotCondition {
	private final int count;

	public RoleCountCondition(int count) {
		Preconditions.checkArgument(count > 0);
		this.count = count;
	}

	@Override
	public StatusResult verify(Robot robot) {
		return ! BlackBoard.loginInfo.isNull(robot) && BlackBoard.loginInfo.get(robot).size() >= count
				? StatusResult.SUCCESS : StatusResult.FAIL;
	}
}
