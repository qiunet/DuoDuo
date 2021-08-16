package org.qiunet.game.test.behavior.node.executor;

import org.qiunet.game.test.behavior.enums.ActionStatus;
import org.qiunet.game.test.behavior.node.IBehaviorNode;
import org.qiunet.game.test.behavior.node.base.BaseBehaviorExecutor;
import org.qiunet.utils.exceptions.CustomException;

/***
 * 或行为逻辑
 *
 * 分为 Priority Selector 和 non-priority Selector
 *
 * 有优先级的节点, 这种选择节点每次都是自左向右依次选择,当发现找到一个可执行的子节点后就停止搜索后续子节点.
 * 这样的选择方式，就存在一个优先级的问题，也就是说最左边的节点优先级最高，因为它是被最先判断的。
 * 对于这种选择节点来说，它的子节点的前提设定，必须是“从窄到宽”的方式，否则后续节点都会发生“饿死”的情况，也就是说永远不会被执行到.
 *
 * 没有优先级的节点. 这种选择节点的选择顺序是从上一个执行过的子节点开始选择，如果前提满足，则继续执行此节点，
 * 如果条件不满足，则从此节点开始，依次判断每一个子节点的前提，当找到一个满足条件的子节点后，则执行该节点
 *
 * @author qiunet
 * 2021-07-07 10:39
 */
public class Selector extends BaseBehaviorExecutor<Selector> {
	/**
	 * 按照优先级执行
	 * 每次从左 -> 右
	 */
	private final boolean prioritySelector;
	/**
	 * 顺序执行的起点索引
	 */
	private int startIndex;
	/**
	 * 当前执行索引
	 */
	private int currIndex;

	public Selector() {
		this(true);
	}

	public Selector(boolean prioritySelector) {
		this.prioritySelector = prioritySelector;
	}

	@Override
	public void initialize() {
		if (nodes.isEmpty()) {
			throw new CustomException("Class [{}] child nodes is empty!", getClass().getName());
		}
		this.currIndex = -1;
	}

	@Override
	public boolean preCondition() {
		for (IBehaviorNode node : nodes) {
			if (node.preCondition()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ActionStatus execute() {
		for (currIndex = this.startIndex(); currIndex < nodes.size(); currIndex++) {
			this.currentBehavior = nodes.get(currIndex);
			if (! prioritySelector) {
				this.startIndex = currIndex;
			}
			boolean preCondition = this.currentBehavior.preCondition();
			if (! preCondition) {
				continue;
			}

			ActionStatus status = this.currentBehavior.run();
			if (status != ActionStatus.FAILURE) {
				return status;
			}

			if (currIndex < nodes.size() - 1) {
				try {
					Thread.sleep(EVERY_NODE_SLEEP_MS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return ActionStatus.FAILURE;
	}

	/**
	 * 获得起始 索引
	 * @return
	 */
	private int startIndex(){
		// 每次从 0 开始
		if (prioritySelector) {
			return 0;
		}
		// 说明已经在运行中 再次进入
		if (currIndex >= 0) {
			return currIndex;
		}

		if (startIndex >= nodes.size()) {
			startIndex = 0;
		}

		return startIndex;
	}
}
