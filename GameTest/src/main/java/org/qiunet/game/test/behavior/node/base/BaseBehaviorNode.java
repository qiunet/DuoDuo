package org.qiunet.game.test.behavior.node.base;

import org.qiunet.game.test.behavior.enums.ActionStatus;
import org.qiunet.game.test.behavior.node.IBehaviorExecutor;
import org.qiunet.game.test.behavior.node.IBehaviorNode;

/***
 *  几点类型
 *
 * @author qiunet
 * 2021-07-05 11:49
 */
abstract class BaseBehaviorNode implements IBehaviorNode {
	/**
	 * 父节点
	 */
	protected IBehaviorExecutor parent;
	/**
	 * 状态
	 */
	protected boolean running;

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public void setParent(IBehaviorExecutor parent) {
		this.parent = parent;
	}

	@Override
	public ActionStatus run() {
		ActionStatus status = execute();
		this.running = status == ActionStatus.RUNNING;
		return status;
	}

	/**
	 * 具体的执行逻辑
	 * @return
	 */
	protected abstract ActionStatus execute();
}
