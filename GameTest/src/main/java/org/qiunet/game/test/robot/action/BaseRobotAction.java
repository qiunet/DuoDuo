package org.qiunet.game.test.robot.action;

import org.qiunet.flash.handler.context.sender.IChannelMessageSender;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.netty.client.param.IClientConfig;
import org.qiunet.function.ai.node.base.BaseBehaviorAction;
import org.qiunet.function.condition.IConditions;
import org.qiunet.game.test.response.IStatusTipsHandler;
import org.qiunet.game.test.robot.Robot;

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
	 * 使用的连接名
	 */
	private final String connectorName;

	public BaseRobotAction(IConditions<Robot> preConditions, String connectorName) {
		super(preConditions);
		this.connectorName = connectorName;
	}

	@Override
	public void prepare() {
		super.prepare();
		getOwner().registerAction(this);
	}

	@Override
	public ISession getSender() {
		return getOwner().getConnector(this.connectorName);
	}

	/**
	 * action 使用自己的连接名连接服务器
	 * action 自己在第一个连接时候. 连接服务器.
	 *
	 * @param config
	 * @return
	 */
	public ISession connector(IClientConfig config) {
		return getOwner().connect(config, this.connectorName);
	}
}
