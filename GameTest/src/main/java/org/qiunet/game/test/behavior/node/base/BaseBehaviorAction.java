package org.qiunet.game.test.behavior.node.base;

import org.qiunet.function.condition.IConditions;
import org.qiunet.game.test.behavior.enums.ActionStatus;
import org.qiunet.game.test.behavior.node.IBehaviorAction;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.utils.exceptions.CustomException;

/***
 * action的 基类
 *
 * @author qiunet
 * 2021-07-07 10:37
 */
public abstract class BaseBehaviorAction extends BaseBehaviorNode implements IBehaviorAction {
	/**
	 * 前置条件
	 */
	protected IConditions<Robot> preCondition;
	public BaseBehaviorAction(IConditions<Robot> preCondition) {
		this.preCondition = preCondition;
	}

	@Override
	public ActionStatus run() {
		if (! isRunning()) {
			return super.run();
		}
		return runningStatusUpdate();
	}

	protected ActionStatus runningStatusUpdate(){
		throw new CustomException("Class [{}] need implements runningStatusUpdate!", getClass().getName());
	}
}
