package org.qiunet.function.ai.node.executor;

import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.ai.node.IBehaviorNode;
import org.qiunet.function.ai.node.base.BaseBehaviorExecutor;
import org.qiunet.utils.exceptions.CustomException;

import java.util.List;

/***
 * 顺序执行容器 (与行为)
 *
 * 如果返回 failure. 则停止执行 返回 failure
 *
 * @author qiunet
 * 2021-07-07 10:39
 */
public class SequenceExecutor extends BaseBehaviorExecutor<SequenceExecutor> {
	/**
	 * 当前执行
	 */
	private int currIndex;


	@Override
	public boolean preCondition() {
		for (IBehaviorNode node : getChildNodes()) {
			if (! node.preCondition()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void initialize() {
		super.initialize();
		if (childSize() == 0) {
			throw new CustomException("Class [{}] child nodes is empty!", getClass().getName());
		}
		this.currIndex = 0;
	}

	@Override
	public ActionStatus execute() {
		List<IBehaviorNode> childNodes = this.getChildNodes();
		for (; currIndex < childSize(); currIndex++) {
			IBehaviorNode currentBehavior = childNodes.get(currIndex);
			if (! currentBehavior.preCondition()) {
				return ActionStatus.FAILURE;
			}

			ActionStatus status = currentBehavior.run();
			if (status != ActionStatus.SUCCESS) {
				return status;
			}
		}
		return ActionStatus.SUCCESS;
	}
}
