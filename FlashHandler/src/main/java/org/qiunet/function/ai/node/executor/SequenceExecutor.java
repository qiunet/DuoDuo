package org.qiunet.function.ai.node.executor;

import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.ai.node.IBehaviorNode;
import org.qiunet.function.ai.node.base.BaseBehaviorExecutor;
import org.qiunet.function.condition.IConditions;
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
public class SequenceExecutor<Owner extends MessageHandler<Owner>> extends BaseBehaviorExecutor<Owner> {
	/**
	 * 当前执行
	 */
	private int currIndex;

	public SequenceExecutor(IConditions<Owner> conditions) {
		this(conditions,  "顺序执行");
	}

	public SequenceExecutor(IConditions<Owner> conditions, String name) {
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
		if (childSize() == 0) {
			throw new CustomException("Class [{}] child nodes is empty!", getClass().getName());
		}
		this.currIndex = 0;
	}

	@Override
	public ActionStatus execute() {
		List<IBehaviorNode<Owner>> childNodes = this.getChildNodes();
		for (; currIndex < childSize(); currIndex++) {
			IBehaviorNode<Owner> currentNode = childNodes.get(currIndex);
			if (!currentNode.isRunning() && ! currentNode.preCondition()) {
				return ActionStatus.FAILURE;
			}

			ActionStatus status = currentNode.run();
			if (status != ActionStatus.SUCCESS) {
				return status;
			}
		}
		return ActionStatus.SUCCESS;
	}
}
