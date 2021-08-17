package org.qiunet.function.ai.node.base;

import org.qiunet.function.ai.node.IBehaviorDecorator;
import org.qiunet.function.ai.node.IBehaviorNode;

/***
 * 装饰节点父类
 *
 * qiunet
 * 2021/8/16 21:38
 **/
public abstract class BaseDecorator extends BaseBehaviorNode implements IBehaviorDecorator {

	/**
	 * 需要翻转的节点
	 */
	protected IBehaviorNode node;

	public BaseDecorator(IBehaviorNode node) {
		this.node = node;
	}

	@Override
	public boolean preCondition() {
		return node.preCondition();
	}
}
