package org.qiunet.function.test.ai.action;

import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.test.ai.Hero;
import org.qiunet.function.test.ai.condition.SeeGoblinCondition;
import org.qiunet.function.test.ai.condition.SeeOmaCondition;

/***
 *
 *
 * qiunet
 * 2021/8/17 10:16
 **/
public class RunToTarget extends BaseAction {

	public RunToTarget(Hero hero) {
		super(hero, new SeeGoblinCondition().and(new SeeOmaCondition().invert()));
	}

	@Override
	protected ActionStatus execute() {
		hero.runToTargetPoint();
		return ActionStatus.RUNNING;
	}

	@Override
	protected ActionStatus runningStatusUpdate() {
		hero.runFinished();
		return ActionStatus.SUCCESS;
	}
}
