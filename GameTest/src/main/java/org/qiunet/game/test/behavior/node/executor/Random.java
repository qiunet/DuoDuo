package org.qiunet.game.test.behavior.node.executor;

import com.google.common.collect.Sets;
import org.qiunet.function.condition.IConditions;
import org.qiunet.game.test.behavior.enums.ActionStatus;
import org.qiunet.game.test.behavior.node.IBehaviorNode;
import org.qiunet.game.test.behavior.node.base.BaseBehaviorExecutor;
import org.qiunet.game.test.robot.Robot;
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
public class Random extends BaseBehaviorExecutor {
	/**
	 * 已经执行过的node
	 * 如果 excludeExecuted == true
	 * 成功 失败都放入.
	 */
	private final Set<IBehaviorNode> executedNodes = Sets.newHashSet();
	/**
	 * 是否排除已经执行过的node
	 */
	private final boolean excludeExecuted;


	public Random(IConditions<Robot> preCondition, String name) {
		this(preCondition, name, false);
	}

	public Random(IConditions<Robot> preCondition, String name, boolean excludeExecuted) {
		super(preCondition, name);
		this.excludeExecuted = excludeExecuted;
	}

	@Override
	public void initialize() {
		if (nodes.isEmpty()) {
			throw new CustomException("Name [{}] child nodes is empty!", getName());
		}

		if (nodes.size() == executedNodes.size()) {
			executedNodes.clear();
		}
	}

	@Override
	public ActionStatus execute() {
		if (isRunning()) {
			// 如果是执行中的对象.直接调用run方法.
			return this.currentBehavior.run();
		}

		HashSet<IBehaviorNode> set = Sets.newHashSet(nodes);
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
