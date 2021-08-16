package org.qiunet.game.test.behavior.node.base;

import org.qiunet.function.condition.IConditions;
import org.qiunet.game.test.behavior.enums.ActionStatus;
import org.qiunet.game.test.behavior.node.IBehaviorExecutor;
import org.qiunet.game.test.behavior.node.IBehaviorNode;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.utils.thread.ThreadContextData;

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
	 * 机器人
	 */
	protected Robot robot;
	/**
	 * 前置条件
	 */
	protected IConditions<Robot> preCondition;
	/**
	 * 状态
	 */
	protected boolean running;


	public BaseBehaviorNode(IConditions<Robot> preCondition) {
		this.robot = ThreadContextData.get(Robot.class.getName());
		this.preCondition = preCondition;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public boolean preCondition() {
		return preCondition.verify(robot).isSuccess();
	}

	@Override
	public void setParent(IBehaviorExecutor parent) {
		this.parent = parent;
	}

	@Override
	public IBehaviorNode getParent() {
		return parent;
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
