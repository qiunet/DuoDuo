package org.qiunet.test.function.test.ai.action;

import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.ai.node.action.BehaviorAction;
import org.qiunet.function.condition.IConditions;
import org.qiunet.test.function.test.ai.Hero;
import org.qiunet.utils.logger.LoggerType;

/***
 *
 *
 * qiunet
 * 2021/8/17 10:16
 **/
@BehaviorAction(desc = "战斗")
public class Fight extends BaseAction {

	public Fight(Hero hero, IConditions<Hero> conditions) {
		super(hero, conditions);
	}

	@Override
	protected ActionStatus execute() {
		LoggerType.DUODUO.info("Hero 攻击哥布林!");
		return ActionStatus.SUCCESS;
	}
}
