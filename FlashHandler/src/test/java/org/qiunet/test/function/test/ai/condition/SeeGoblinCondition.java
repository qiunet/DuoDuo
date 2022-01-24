package org.qiunet.test.function.test.ai.condition;

import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.function.condition.ConditionField;
import org.qiunet.function.condition.ICondition;
import org.qiunet.test.function.test.ai.Hero;
import org.qiunet.test.function.test.ai.enums.Enemy;

/***
 *
 *
 * qiunet
 * 2021/8/17 10:21
 **/
public class SeeGoblinCondition implements ICondition<Hero, ConditionType> {

	@ConditionField(desc = "看见的数量")
	private int count;

	@Override
	public StatusResult verify(Hero hero) {
		return hero.isSee(Enemy.GOBLIN) ? StatusResult.SUCCESS : StatusResult.FAIL;
	}

	@Override
	public ConditionType getType() {
		return ConditionType.SEE_GOBLIN;
	}
}
