package org.qiunet.game.tests.client.action.login;

import org.qiunet.function.ai.builder.IBehaviorBuilder;
import org.qiunet.function.ai.node.IBehaviorNode;
import org.qiunet.function.ai.node.executor.SelectorExecutor;
import org.qiunet.function.ai.node.executor.SequenceExecutor;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.tests.client.data.condition.AuthCondition;

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
	public IBehaviorNode buildExecutor(Robot robot) {
		SequenceExecutor sequence = new SequenceExecutor(() -> new AuthCondition().not().verify(robot).isSuccess());
		SequenceExecutor registerBehavior = new SequenceExecutor().addChild(new RandomNameAction(robot), new RegisterAction(robot), new PlayerIndexAction(robot));
		sequence.addChild(new LoginAction(robot), new SelectorExecutor().addChild(registerBehavior, new PlayerIndexAction(robot)));
		return sequence;
	}
}
