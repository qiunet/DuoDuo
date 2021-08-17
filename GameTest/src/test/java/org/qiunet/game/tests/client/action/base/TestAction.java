package org.qiunet.game.tests.client.action.base;

import org.qiunet.flash.handler.context.sender.IChannelMessageSender;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.function.condition.IConditions;
import org.qiunet.game.test.behavior.node.base.BaseBehaviorAction;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.test.server.IServer;
import org.qiunet.game.tests.server.type.ServerType;
import org.qiunet.utils.thread.ThreadContextData;

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
	private final Robot robot;
	/**
	 * 使用的连接方式
	 */
	private final IServer server = ServerType.LC_ONLINE;

	public TestAction(IConditions<Robot> preConditions) {
		super(preConditions);
		this.robot = ThreadContextData.get(Robot.class.getName());
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
