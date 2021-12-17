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
 * 2021/8/17 10:37
 **/
@BehaviorAction(name = "逃跑")
public class Escape extends BaseAction {

	public Escape(IConditions<Hero> conditions) {
		super(conditions);
	}

	@Override
	protected ActionStatus execute() {
		LoggerType.DUODUO.info("Hero 逃跑!");
		return ActionStatus.SUCCESS;
	}
}
