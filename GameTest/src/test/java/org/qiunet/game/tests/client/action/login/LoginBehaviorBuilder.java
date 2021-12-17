package org.qiunet.game.tests.client.action.login;

import org.qiunet.function.ai.node.IBehaviorExecutor;
import org.qiunet.function.ai.node.IBehaviorNode;
import org.qiunet.function.ai.node.decorator.CounterNode;
import org.qiunet.function.ai.node.executor.SequenceExecutor;
import org.qiunet.game.test.bt.IBehaviorBuilder;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.tests.client.data.condition.AuthCondition;
import org.qiunet.game.tests.client.data.condition.RegisterCountCondition;

/***
 * 登录的行为树构造.
 *
 * 先拉去 loginInfo list
 * 然后概率选取注册 或者 直接用list最后一个login info(在真实客户端. 这里应该是玩家选择角色), 请求playerIndex
 *
 * qiunet
 * 2021/8/16 21:58
 **/
public class LoginBehaviorBuilder implements IBehaviorBuilder<Robot> {

	@Override
	public IBehaviorNode<Robot> buildExecutor(Robot robot) {
		SequenceExecutor<Robot> sequence = new SequenceExecutor<>(new AuthCondition().not());

		IBehaviorExecutor<Robot> registerBehavior = new SequenceExecutor<>(new RegisterCountCondition(3))
				.addChild(new RandomNameAction(robot), new RegisterAction(robot));

		sequence.addChild(new LoginAction(robot), registerBehavior, new PlayerIndexAction(robot));
		return new CounterNode(sequence, 1);
	}
}
