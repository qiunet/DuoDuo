package org.qiunet.game.tests.client.action.login;

import org.qiunet.game.test.behavior.builder.IBehaviorBuilder;
import org.qiunet.game.test.behavior.node.IBehaviorNode;
import org.qiunet.game.test.behavior.node.decorator.CounterNode;
import org.qiunet.game.test.behavior.node.executor.Selector;
import org.qiunet.game.test.behavior.node.executor.Sequence;
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
		sequence.addChild(new LoginAction());
		Sequence registerBehavior = new Sequence().addChild(new RandomAction(), new RegisterAction(), new PlayerIndexAction());
		sequence.addChild(new Selector().addChild(registerBehavior, new PlayerIndexAction()));
		return new CounterNode(sequence, 1);
	}
}
