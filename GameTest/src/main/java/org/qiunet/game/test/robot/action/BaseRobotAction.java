package org.qiunet.game.test.robot.action;

import org.qiunet.flash.handler.context.sender.IChannelMessageSender;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.function.ai.node.base.BaseBehaviorAction;
import org.qiunet.function.condition.IConditions;
import org.qiunet.game.test.response.IStatusTipsHandler;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.test.server.IServer;

/***
 * 基础的 ActionNode
 * 目的省略action node的ServerType 参数
 *
 * qiunet
 * 2021/8/8 11:46
 **/
public abstract class BaseRobotAction extends BaseBehaviorAction<Robot>
		implements IChannelMessageSender, IStatusTipsHandler {
	/**
	 * 使用的连接方式
	 */
	private final IServer server;

	public BaseRobotAction(IConditions<Robot> preConditions, IServer server) {
		super(preConditions);
		this.server = server;
	}

	@Override
	public void prepare() {
		super.prepare();
		getOwner().registerAction(this);
	}

	@Override
	public DSession getSender() {
		return getOwner().getPersistConnClient(server);
	}
}
