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
 * 2021/8/17 10:16
 **/
public class Fight extends BaseAction {

	public Fight(Hero hero) {
		super(hero, new SeeGoblinCondition().and(new SeeOmaCondition().not()));
	}

	@Override
	protected ActionStatus execute() {
		LoggerType.DUODUO.info("Hero 攻击哥布林!");
		return ActionStatus.SUCCESS;
	}
}
