package org.qiunet.test.function.test.ai.condition;

import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.function.condition.ICondition;
import org.qiunet.test.function.test.ai.Hero;
import org.qiunet.test.function.test.ai.enums.Enemy;

/***
 *
 *
 * qiunet
 * 2021/8/17 10:21
 **/
public class SeeOmaCondition implements ICondition<Hero, ConditionType> {
	@Override
	public StatusResult verify(Hero hero) {
		return hero.isSee(Enemy.OMA) ? StatusResult.SUCCESS : StatusResult.FAIL;
	}

	@Override
	public ConditionType getType() {
		return ConditionType.SEE_OMA;
	}
}
