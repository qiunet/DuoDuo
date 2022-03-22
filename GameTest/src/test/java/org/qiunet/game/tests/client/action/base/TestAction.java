package org.qiunet.game.tests.client.action.base;

import org.qiunet.function.condition.IConditions;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.test.robot.action.BaseRobotAction;
import org.qiunet.game.tests.server.enums.ServerType;

/***
 * 基础的TestActionNode
 * 目的省略action node的ServerType 参数
 *
 * qiunet
 * 2021/8/8 11:46
 **/
public abstract class TestAction extends BaseRobotAction {

	public TestAction(IConditions<Robot> preConditions) {
		super(preConditions, ServerType.LC_ROOM.getName());
	}
}
