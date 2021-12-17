package org.qiunet.test.function.test.ai.action;

import org.qiunet.function.ai.node.base.BaseBehaviorAction;
import org.qiunet.function.condition.IConditions;
import org.qiunet.test.function.test.ai.Hero;

/***
 *
 *
 * qiunet
 * 2021/8/17 10:16
 **/
public abstract class BaseAction extends BaseBehaviorAction<Hero> {

	public BaseAction(IConditions<Hero> preCondition) {
		super(preCondition);
	}
}
