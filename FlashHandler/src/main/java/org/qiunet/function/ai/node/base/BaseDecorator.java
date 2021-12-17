package org.qiunet.function.ai.node.base;

import com.google.common.collect.ImmutableList;
import org.qiunet.function.ai.node.IBehaviorDecorator;
import org.qiunet.function.ai.node.IBehaviorExecutor;
import org.qiunet.function.ai.node.IBehaviorNode;
import org.qiunet.utils.exceptions.CustomException;

import java.util.List;

/***
 * 装饰节点父类
 *
 * qiunet
 * 2021/8/16 21:38
 **/
public abstract class BaseDecorator<Owner> extends BaseBehaviorNode<Owner> implements IBehaviorDecorator<Owner> {

	/**
	 * 需要翻转的节点
	 */
	protected IBehaviorNode<Owner> node;
	private final List<IBehaviorNode<Owner>> nodes;

	public BaseDecorator(IBehaviorNode<Owner> node) {
		super(null, node.getName());
		this.nodes = ImmutableList.of(node);
		node.setParent(this);
		this.node = node;
	}

	@Override
	public IBehaviorExecutor<Owner> addChild(IBehaviorNode<Owner>... actions) {
		throw new CustomException("Not support in decorator node!");
	}

	@Override
	public List<IBehaviorNode<Owner>> getChildNodes() {
		return this.nodes;
	}

	@Override
	public IBehaviorNode<Owner> getNode() {
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
