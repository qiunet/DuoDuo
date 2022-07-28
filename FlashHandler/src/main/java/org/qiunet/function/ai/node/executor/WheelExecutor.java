package org.qiunet.function.ai.node.executor;

import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.ai.node.IBehaviorNode;
import org.qiunet.function.ai.node.base.BaseBehaviorExecutor;
import org.qiunet.function.condition.IConditions;

import java.util.List;

/***
 * 轮流执行.
 * 执行到成功为止.
 * 下次从下一个开始
 *
 * @author qiunet
 * 2022-07-28 10:39
 */
public class WheelExecutor<Owner> extends BaseBehaviorExecutor<Owner> {
	/**
     * 当前执行索引
	 */
	private int currIndex;

	public WheelExecutor(IConditions<Owner> conditions) {
		this(conditions, "轮流执行");
	}

	public WheelExecutor(IConditions<Owner> conditions, String name) {
		super(conditions, name);
	}

	@Override
	public void reset() {
		super.reset();
		this.currIndex = 0;
	}

	@Override
	public void initialize() {
		super.initialize();
	}

	@Override
	public ActionStatus execute() {
		List<IBehaviorNode<Owner>> childNodes = this.getChildNodes();
		int limit = childSize();
		for (currIndex = this.currIndex(); currIndex < limit; currIndex++) {
			IBehaviorNode<Owner> currentNode = childNodes.get(currIndex);

			boolean preCondition = currentNode.isRunning() || currentNode.preCondition();
			if (! preCondition) {
				continue;
			}

			ActionStatus status = currentNode.run();
			if (status == ActionStatus.RUNNING) {
				return status;
			}

			if (status == ActionStatus.SUCCESS) {
				currIndex ++;
				return status;
			}
		}
		return ActionStatus.FAILURE;
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
