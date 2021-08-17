package org.qiunet.function.test.ai.action;

import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.test.ai.Hero;
import org.qiunet.function.test.ai.condition.SeeGoblinCondition;
import org.qiunet.function.test.ai.condition.SeeOmaCondition;
import org.qiunet.utils.logger.LoggerType;

/***
 *
 *
 * qiunet
 * 2021/8/17 10:16
 **/
public class Fight extends BaseAction {

	public Fight(Hero hero) {
		super(hero, new SeeGoblinCondition().and(new SeeOmaCondition().invert()));
	}

	@Override
	protected ActionStatus execute() {
		LoggerType.DUODUO.info("Hero 攻击哥布林!");
		return ActionStatus.SUCCESS;
	}
}
