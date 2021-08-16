package org.qiunet.game.test.behavior.node.base;

import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.function.condition.IConditions;
import org.qiunet.game.test.behavior.node.IBehaviorAction;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.test.server.IServer;

/***
 * action的 基类
 *
 * @author qiunet
 * 2021-07-07 10:37
 */
public abstract class BaseBehaviorAction extends BaseBehaviorNode implements IBehaviorAction {
	/**
	 * 使用的连接方式
	 */
	private final IServer server;

	public BaseBehaviorAction(IServer server, IConditions<Robot> preCondition) {
		super(preCondition);
		this.server = server;
	}

	@Override
	public DSession getSession() {
		return robot.getPersistConnClient(server);
	}
}
