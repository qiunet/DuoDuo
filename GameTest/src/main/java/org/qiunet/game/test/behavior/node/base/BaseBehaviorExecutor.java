package org.qiunet.game.test.behavior.node.base;

import com.google.common.collect.Lists;
import org.qiunet.game.test.behavior.node.IBehaviorExecutor;
import org.qiunet.game.test.behavior.node.IBehaviorNode;

import java.util.List;

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
	protected final List<IBehaviorNode> nodes = Lists.newArrayList();
	/**
	 * 当前执行的节点
	 * 如果是run状态. 直接执行该对象的update.
	 */
	protected IBehaviorNode currentBehavior;

	@Override
	public void removeChild(IBehaviorNode child) {
		nodes.remove(child);
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
	public void initialize() {
		nodes.forEach(IBehaviorNode::initialize);
	}

	@Override
	public void release() {
		nodes.forEach(IBehaviorNode::release);
	}
}
