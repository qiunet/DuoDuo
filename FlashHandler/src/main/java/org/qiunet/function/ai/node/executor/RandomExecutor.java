package org.qiunet.function.ai.node.executor;

import com.google.common.collect.Sets;
import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.ai.node.IBehaviorNode;
import org.qiunet.function.ai.node.base.BaseBehaviorExecutor;
import org.qiunet.function.condition.IConditions;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.math.MathUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/***
 *
 *
 * qiunet
 * 2021/8/9 12:10
 **/
public class RandomExecutor<Owner extends MessageHandler<Owner>> extends BaseBehaviorExecutor<Owner> {
	/**
	 * 已经执行过的node
	 * 如果 excludeExecuted == true
	 * 成功 失败都放入.
	 */
	private final Set<IBehaviorNode> executedNodes = Sets.newHashSet();
	/**
	 * 当前执行的节点
	 */
	private IBehaviorNode currentBehavior;
	/**
	 * 是否排除已经执行过的node
	 */
	private final boolean excludeExecuted;


	public RandomExecutor(IConditions<Owner> conditions) {
		this(conditions, "随机节点");
	}

	public RandomExecutor(IConditions<Owner> conditions, String name) {
		this(conditions, name, false);
	}
	public RandomExecutor(IConditions<Owner> conditions, String name, boolean excludeExecuted) {
		super(conditions, name);
		this.excludeExecuted = excludeExecuted;
	}

	@Override
	public boolean preCondition() {
		if (! super.preCondition()) {
			return false;
		}

		for (IBehaviorNode node : getChildNodes()) {
			if (executedNodes.contains(node)) {
				continue;
			}

			if (node.isRunning() || node.preCondition()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void initialize() {
		super.initialize();

		if (getChildNodes().isEmpty()) {
			throw new CustomException("Class [{}] child nodes is empty!", getClass().getName());
		}


		if (childSize() == executedNodes.size()) {
			executedNodes.clear();
		}
		this.currentBehavior = null;
	}

	@Override
	public void reset() {
		super.reset();
		this.currentBehavior = null;
		this.executedNodes.clear();
	}

	@Override
	public ActionStatus execute() {
		if (isRunning()) {
			// 如果是执行中的对象.直接调用run方法.
			return this.currentBehavior.run();
		}

		HashSet<IBehaviorNode> set = Sets.newHashSet(getChildNodes());
		set.removeAll(executedNodes);

		int totalWeight = 0;
		List<IBehaviorNode> list = new ArrayList<>(set.size());
		for (IBehaviorNode behaviorNode : set) {
			if (! behaviorNode.preCondition()) {
				continue;
			}
			totalWeight += behaviorNode.weight();
			list.add(behaviorNode);
		}

		this.currentBehavior = MathUtil.randByWeight(list, totalWeight);
		if (currentBehavior == null) {
			return ActionStatus.FAILURE;
		}

		if (excludeExecuted) {
			executedNodes.add(currentBehavior);
		}
		return currentBehavior.run();
	}
}
