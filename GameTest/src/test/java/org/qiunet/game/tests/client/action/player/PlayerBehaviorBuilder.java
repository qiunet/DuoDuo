package org.qiunet.game.tests.client.action.player;

import org.qiunet.function.ai.node.IBehaviorNode;
import org.qiunet.function.ai.node.executor.SelectorExecutor;
import org.qiunet.game.test.bt.IBehaviorBuilder;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.tests.client.data.condition.AuthCondition;

/***
 *
 *
 * qiunet
 * 2021/9/2 15:06
 **/
public class PlayerBehaviorBuilder implements IBehaviorBuilder<Robot> {
	@Override
	public IBehaviorNode<Robot> buildExecutor(Robot robot) {
		SelectorExecutor<Robot> selectorExecutor = new SelectorExecutor<>(new AuthCondition());
		selectorExecutor.addChild(new GetExpAction(robot), new UpgradeLvAction(robot));
		return selectorExecutor;
	}
}
