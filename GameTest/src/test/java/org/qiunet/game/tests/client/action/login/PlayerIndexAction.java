package org.qiunet.game.tests.client.action.login;

import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.tests.client.action.base.TestAction;
import org.qiunet.game.tests.client.data.condition.AuthCondition;

/***
 *
 *
 * qiunet
 * 2021/8/16 21:07
 **/
public class PlayerIndexAction extends TestAction {

	public PlayerIndexAction(Robot robot) {
		super(robot, new AuthCondition());
	}

	@Override
	protected ActionStatus execute() {
		return null;
	}
}
