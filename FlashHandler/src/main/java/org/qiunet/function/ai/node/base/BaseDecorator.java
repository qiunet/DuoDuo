package org.qiunet.function.ai.node.base;

import com.google.common.collect.ImmutableList;
import org.qiunet.function.ai.node.IBehaviorDecorator;
import org.qiunet.function.ai.node.IBehaviorNode;

import java.util.List;

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
	private final List<IBehaviorNode> nodes;

	public BaseDecorator(IBehaviorNode node) {
		this.node = node;
		node.setParent(this);
		this.nodes = ImmutableList.of(this.node);
	}

	@Override
	public List<IBehaviorNode> getChildNodes() {
		return this.nodes;
	}

	@Override
	public IBehaviorNode getNode() {
		return node;
	}

	@Override
	public boolean preCondition() {
		if (node.isRunning()) {
			return true;
		}
		return node.preCondition();
	}
}
