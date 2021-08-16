package org.qiunet.game.test.behavior.node.base;

import com.google.common.collect.Lists;
import org.qiunet.function.condition.IConditions;
import org.qiunet.game.test.behavior.node.IBehaviorExecutor;
import org.qiunet.game.test.behavior.node.IBehaviorNode;
import org.qiunet.game.test.robot.Robot;

import java.util.List;

/***
 * 行为树的执行容器
 *
 * @author qiunet
 * 2021-07-08 10:50
 */
public abstract class BaseBehaviorExecutor extends BaseBehaviorNode implements IBehaviorExecutor {
	/**
	 * 节点内所有的Node
	 */
	protected final List<IBehaviorNode> nodes = Lists.newArrayList();
	/**
	 * 执行器的名称.
	 * 比如登入游戏  升级装备 等
	 */
	protected String name;
	/**
	 * 当前执行的节点
	 * 如果是run状态. 直接执行该对象的update.
	 */
	protected IBehaviorNode currentBehavior;
	public BaseBehaviorExecutor(IConditions<Robot> preCondition, String name) {
		super(preCondition);
		this.name = name;
	}

	@Override
	public void removeChild(IBehaviorNode child) {
		nodes.remove(child);
	}

	@Override
	public List<IBehaviorNode> getChildren() {
		return Lists.newArrayList(nodes);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public IBehaviorNode currentBehavior() {
		return currentBehavior;
	}

	/**
	 * 添加action
	 * @param actions
	 */
	public void addChild(IBehaviorNode... actions) {
		for (IBehaviorNode action : actions) {
			action.setParent(this);
			this.nodes.add(action);
		}
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
