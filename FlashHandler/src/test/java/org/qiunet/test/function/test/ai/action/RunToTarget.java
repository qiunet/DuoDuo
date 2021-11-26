package org.qiunet.test.function.test.ai.action;

import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.test.function.test.ai.Hero;
import org.qiunet.test.function.test.ai.condition.SeeGoblinCondition;
import org.qiunet.test.function.test.ai.condition.SeeOmaCondition;

/***
 *
 *
 * qiunet
 * 2021/8/17 10:16
 **/
public class RunToTarget extends BaseAction {

	public RunToTarget(Hero hero) {
		super(hero, new SeeGoblinCondition().and(new SeeOmaCondition().not()));
	}

	@Override
	protected ActionStatus execute() {
		hero.runToTargetPoint();
		return ActionStatus.RUNNING;
	}

	@Override
	protected ActionStatus runningStatusUpdate() {
		// 正常. 这里判断是否到坐标点了. 没有到返回RUNNING
		hero.runFinished();
		return ActionStatus.SUCCESS;
	}
}
