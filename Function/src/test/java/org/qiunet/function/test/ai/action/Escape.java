package org.qiunet.function.test.ai.action;

import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.test.ai.Hero;
import org.qiunet.function.test.ai.condition.SeeOmaCondition;
import org.qiunet.utils.logger.LoggerType;

/***
 *
 *
 * qiunet
 * 2021/8/17 10:37
 **/
public class Escape extends BaseAction{
	public Escape(Hero hero) {
		super(hero, new SeeOmaCondition());
	}

	@Override
	protected ActionStatus execute() {
		LoggerType.DUODUO.info("Hero 逃跑!");
		return ActionStatus.SUCCESS;
	}
}
