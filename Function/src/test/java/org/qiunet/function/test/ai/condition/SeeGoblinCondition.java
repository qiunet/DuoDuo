package org.qiunet.function.test.ai.condition;

import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.function.condition.IConditions;
import org.qiunet.function.test.ai.Hero;
import org.qiunet.function.test.ai.enums.Enemy;

/***
 *
 *
 * qiunet
 * 2021/8/17 10:21
 **/
public class SeeGoblinCondition implements IConditions<Hero> {
	@Override
	public StatusResult verify(Hero hero) {
		return hero.isSee(Enemy.GOBLIN) ? StatusResult.SUCCESS : StatusResult.FAIL;
	}
}
