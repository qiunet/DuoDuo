package org.qiunet.function.test.ai.action;

import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.test.ai.Hero;
import org.qiunet.function.test.ai.condition.SeeGoblinCondition;
import org.qiunet.function.test.ai.condition.SeeOmaCondition;
import org.qiunet.utils.logger.LoggerType;

/***
 * 获取经验
 *
 * qiunet
 * 2021/8/17 10:39
 **/
public class GetExp extends BaseAction {

	public GetExp(Hero hero) {
		super(hero, new SeeGoblinCondition().invert().and(new SeeOmaCondition().invert()));
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
