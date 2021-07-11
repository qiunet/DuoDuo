package org.qiunet.game.test.behavior.node.base;

import org.qiunet.game.test.behavior.action.ActionStatus;
import org.qiunet.game.test.behavior.action.IBehaviorNode;
import org.qiunet.game.test.robot.Robot;

/***
 *  几点类型
 *
 * @author qiunet
 * 2021-07-05 11:49
 */
abstract class BaseNode implements IBehaviorNode {

	/**
	 * 节点状态.
	 */
	protected ActionStatus status = ActionStatus.INIT;
	/**
	 * 父节点
	 */
	protected BaseNode parent;
	/**
	 * 机器人
	 */
	protected Robot robot;

	public BaseNode(Robot robot) {
		this.robot = robot;
	}

	@Override
	public ActionStatus getStatus() {
		return status;
	}

	@Override
	public IBehaviorNode parent() {
		return parent;
	}
}
