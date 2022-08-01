package org.qiunet.function.ai.node.executor;

import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.ai.node.IBehaviorNode;
import org.qiunet.function.ai.node.base.BaseBehaviorExecutor;
import org.qiunet.function.condition.IConditions;
import org.qiunet.utils.async.LazyLoader;
import org.qiunet.utils.collection.enums.ForEachResult;
import org.qiunet.utils.collection.wheel.WheelList;
import org.qiunet.utils.common.functional.IndexForeach;

import java.util.concurrent.atomic.AtomicReference;

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
public class SelectorExecutor<Owner extends MessageHandler<Owner>> extends BaseBehaviorExecutor<Owner> {
	/**
	 * 按照优先级执行
	 * 每次从左 -> 右
	 */
	private final boolean prioritySelector;
	/**
     * 当前执行索引
	 */
	private int currIndex;

	public SelectorExecutor(IConditions<Owner> conditions) {
		this(conditions, "选择执行");
	}

	public SelectorExecutor(IConditions<Owner> conditions, String name) {
		this(conditions, name, true);
	}
	public SelectorExecutor(IConditions<Owner> conditions, String name, boolean prioritySelector) {
		super(conditions, name);
		this.prioritySelector = prioritySelector;
	}

	@Override
	public void reset() {
		super.reset();
		this.currIndex = 0;
	}

	@Override
	public void initialize() {
		super.initialize();
		if (prioritySelector) {
			// 优先级节点.每次都从 0 开始
			this.currIndex = 0;
		}
	}
	private final LazyLoader<WheelList<IBehaviorNode<Owner>>> wheelList = new LazyLoader<>(() -> new WheelList<>(this.getChildNodes()));
	@Override
	public ActionStatus execute() {
		AtomicReference<ActionStatus> statusRef = new AtomicReference<>(ActionStatus.FAILURE);
		IndexForeach<IBehaviorNode<Owner>> consumer = (index, currentNode) -> {
			boolean preCondition = currentNode.isRunning() || currentNode.preCondition();
			if (! preCondition) {
				return ForEachResult.CONTINUE;
			}

			statusRef.set(currentNode.run());
			if (statusRef.get() != ActionStatus.FAILURE) {
				return ForEachResult.BREAK;
			}
			return ForEachResult.CONTINUE;
		};
		if (isRunning()) {
			currIndex = wheelList.get().foreach(currIndex, consumer);
		}else {
			currIndex = wheelList.get().nextForeach(currIndex, consumer);
		}
		return statusRef.get();
	}

	/**
	 * 获得起始 索引
	 * @return
	 */
	private int currIndex(){
		// 说明已经在运行中 再次进入
		if (currIndex >= childSize()) {
			return 0;
		}

		return currIndex;
	}
}
