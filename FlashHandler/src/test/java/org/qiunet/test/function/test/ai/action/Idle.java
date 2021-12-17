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
 * 2021/8/17 10:39
 **/
@BehaviorAction(name = "空闲")
public class Idle extends BaseAction {

	public Idle(IConditions<Hero> conditions) {
		super(conditions);
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
