package org.qiunet.test.function.test.ai.action;

import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.ai.node.action.BehaviorAction;
import org.qiunet.function.condition.IConditions;
import org.qiunet.test.function.test.ai.Hero;
import org.qiunet.utils.logger.LoggerType;

/***
 * 获取经验
 *
 * qiunet
 * 2021/8/17 10:39
 **/
@BehaviorAction(name = "打经验")
public class GetExp extends BaseAction {

	public GetExp(IConditions<Hero> conditions) {
		super(conditions);
	}

	@Override
	protected ActionStatus execute() {
		LoggerType.DUODUO.info("Hero 刷经验...");
		return ActionStatus.SUCCESS;
	}

	@Override
	public int weight() {
		return 3;
	}
}
