package org.qiunet.game.test.behavior.node.base;

import org.qiunet.function.condition.ConditionManager;
import org.qiunet.function.condition.IConditions;
import org.qiunet.game.test.behavior.node.IBehaviorAction;
import org.qiunet.game.test.robot.Robot;

/***
 * 装饰节点父类
 *
 * qiunet
 * 2021/8/16 21:38
 **/
public abstract class BaseDecorator extends BaseBehaviorNode{

	/**
	 * 需要翻转的节点
	 */
	protected IBehaviorAction action;

	public BaseDecorator(IBehaviorAction action) {
		super((IConditions<Robot>) ConditionManager.EMPTY_CONDITION);
		this.action = action;
	}
}
