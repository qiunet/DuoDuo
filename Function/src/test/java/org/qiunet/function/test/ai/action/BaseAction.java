package org.qiunet.function.test.ai.action;

import org.qiunet.function.ai.node.base.BaseBehaviorAction;
import org.qiunet.function.condition.IConditions;
import org.qiunet.function.test.ai.Hero;

/***
 *
 *
 * qiunet
 * 2021/8/17 10:16
 **/
public abstract class BaseAction extends BaseBehaviorAction {

	protected final Hero hero;

	private final IConditions<Hero> preCondition;

	public BaseAction(Hero hero, IConditions<Hero> preCondition) {
		this.hero = hero;
		this.preCondition = preCondition;
	}

	@Override
	public boolean preCondition() {
		return preCondition.verify(hero).isSuccess();
	}
}
