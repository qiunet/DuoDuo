package org.qiunet.game.tests.client.action.base;

import org.qiunet.flash.handler.context.sender.IChannelMessageSender;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.function.ai.node.base.BaseBehaviorAction;
import org.qiunet.function.condition.IConditions;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.test.server.IServer;
import org.qiunet.game.tests.server.type.ServerType;

/***
 * 基础的TestActionNode
 * 目的省略action node的ServerType 参数
 *
 * qiunet
 * 2021/8/8 11:46
 **/
public abstract class TestAction extends BaseBehaviorAction implements IChannelMessageSender {
	/**
	 * 机器人
	 */
	protected final Robot robot;
	/**
	 * 前置条件
	 */
	protected IConditions<Robot> preCondition;
	/**
	 * 使用的连接方式
	 */
	private final IServer server = ServerType.LC_ONLINE;

	public TestAction(Robot robot, IConditions<Robot> preConditions) {
		this.preCondition = preConditions;
		this.robot = robot;
	}

	@Override
	public boolean preCondition() {
		return preCondition.verify(robot).isSuccess();
	}

	@Override
	public DSession getSession() {
		return robot.getPersistConnClient(server);
	}
}
