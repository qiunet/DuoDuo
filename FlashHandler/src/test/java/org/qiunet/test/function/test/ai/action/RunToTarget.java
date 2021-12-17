package org.qiunet.test.function.test.ai.action;

import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.ai.node.action.BehaviorAction;
import org.qiunet.function.condition.IConditions;
import org.qiunet.test.function.test.ai.Hero;

/***
 *
 *
 * qiunet
 * 2021/8/17 10:16
 **/
@BehaviorAction(name = "跑向目标")
public class RunToTarget extends BaseAction {

	public RunToTarget(IConditions<Hero> conditions) {
		super(conditions);
	}

	@Override
	protected ActionStatus execute() {
		getOwner().runToTargetPoint();
		return ActionStatus.RUNNING;
	}

	@Override
	protected ActionStatus runningStatusUpdate() {
		// 正常. 这里判断是否到坐标点了. 没有到返回RUNNING
		getOwner().runFinished();
		return ActionStatus.SUCCESS;
	}
}
