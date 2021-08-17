package org.qiunet.game.tests.client.action.login;

import org.qiunet.function.ai.builder.IBehaviorBuilder;
import org.qiunet.function.ai.node.IBehaviorNode;
import org.qiunet.function.ai.node.decorator.CounterNode;
import org.qiunet.function.ai.node.executor.Selector;
import org.qiunet.function.ai.node.executor.Sequence;
import org.qiunet.game.test.robot.Robot;

/***
 * 登录的行为树构造.
 *
 * qiunet
 * 2021/8/16 21:58
 **/
public class LoginBehaviorBuilder implements IBehaviorBuilder<Robot> {

	@Override
	public IBehaviorNode buildExecutor(Robot robot) {
		Sequence sequence = new Sequence();
		sequence.addChild(new LoginAction(robot));
		Sequence registerBehavior = new Sequence().addChild(new RandomAction(robot), new RegisterAction(robot), new PlayerIndexAction(robot));
		sequence.addChild(new Selector().addChild(registerBehavior, new PlayerIndexAction(robot)));
		return new CounterNode(sequence, 1);
	}
}
