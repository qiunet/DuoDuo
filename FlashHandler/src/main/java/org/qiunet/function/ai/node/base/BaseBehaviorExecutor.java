package org.qiunet.function.ai.node.base;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.qiunet.function.ai.node.IBehaviorExecutor;
import org.qiunet.function.ai.node.IBehaviorNode;

import java.util.List;
import java.util.function.Supplier;

/***
 * 行为树的执行容器
 *
 * @author qiunet
 * 2021-07-08 10:50
 */
public abstract class BaseBehaviorExecutor<T extends BaseBehaviorExecutor<T>> extends BaseBehaviorNode implements IBehaviorExecutor {
	/**
	 * 节点内所有的Node
	 */
	private final List<IBehaviorNode> nodes = Lists.newArrayList();
	/**
	 * 条件结果.
	 */
	private final Supplier<Boolean> conditionResult;

	public BaseBehaviorExecutor(Supplier<Boolean> conditionResult) {
		this.conditionResult = conditionResult;
	}

	public BaseBehaviorExecutor() {
		this(null);
	}

	@Override
	public void removeChild(IBehaviorNode child) {
		nodes.remove(child);
		this.check();
	}

	@Override
	public int childSize() {
		return nodes.size();
	}

	/**
	 * 添加action
	 * @param actions
	 */
	public T addChild(IBehaviorNode... actions) {
		for (IBehaviorNode action : actions) {
			action.setParent(this);
			this.nodes.add(action);
		}
		return (T)this;
	}

	@Override
	public List<IBehaviorNode> getChildNodes() {
		return ImmutableList.copyOf(nodes);
	}

	@Override
	public void initialize() {
		nodes.forEach(IBehaviorNode::initialize);
	}

	@Override
	public boolean preCondition() {
		if (conditionResult == null) {
			return true;
		}
		return conditionResult.get();
	}

	@Override
	public void reset() {
		nodes.forEach(IBehaviorNode::reset);
	}

	@Override
	public void release() {
		nodes.forEach(IBehaviorNode::release);
	}
}
