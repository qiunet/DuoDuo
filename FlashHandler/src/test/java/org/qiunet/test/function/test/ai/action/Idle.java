package org.qiunet.test.function.test.ai.action;

import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.test.function.test.ai.Hero;
import org.qiunet.test.function.test.ai.condition.SeeGoblinCondition;
import org.qiunet.test.function.test.ai.condition.SeeOmaCondition;
import org.qiunet.utils.logger.LoggerType;

/***
 *
 *
 * qiunet
 * 2021/8/17 10:39
 **/
public class Idle extends BaseAction {

	public Idle(Hero hero) {
		super(hero, new SeeGoblinCondition().not().and(new SeeOmaCondition().not()));
	}

	@Override
	protected ActionStatus execute() {
		LoggerType.DUODUO.info("Hero 什么也不干");
		return ActionStatus.SUCCESS;
	}

	@Override
	public int weight() {
		return 2;
	}
}
