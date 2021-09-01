package org.qiunet.game.tests.client.action.login;

import org.qiunet.function.ai.builder.IBehaviorBuilder;
import org.qiunet.function.ai.node.IBehaviorNode;
import org.qiunet.function.ai.node.decorator.CounterNode;
import org.qiunet.function.ai.node.executor.SelectorExecutor;
import org.qiunet.function.ai.node.executor.SequenceExecutor;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.tests.client.data.condition.AuthCondition;

/***
 * 登录的行为树构造.
 *
 * qiunet
 * 2021/8/16 21:58
 **/
public class LoginBehaviorBuilder implements IBehaviorBuilder<Robot> {

	@Override
	public IBehaviorNode buildExecutor(Robot robot) {
		SequenceExecutor sequence = new SequenceExecutor(() -> new AuthCondition().invert().verify(robot).isSuccess());
		SequenceExecutor registerBehavior = new SequenceExecutor().addChild(new RandomNameAction(robot), new RegisterAction(robot), new PlayerIndexAction(robot));
		sequence.addChild(new LoginAction(robot), new SelectorExecutor().addChild(registerBehavior, new PlayerIndexAction(robot)));
		return new CounterNode(sequence, 1);
	}
}
