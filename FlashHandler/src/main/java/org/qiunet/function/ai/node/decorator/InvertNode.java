package org.qiunet.function.ai.node.decorator;

import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.ai.node.IBehaviorNode;
import org.qiunet.function.ai.node.base.BaseDecorator;

/***
 * 使颠倒
 *
 * qiunet
 * 2021/8/16 21:36
 **/
public class InvertNode<Owner> extends BaseDecorator<Owner> {

	public InvertNode(IBehaviorNode<Owner> node) {
		super(node);
	}

	@Override
	protected ActionStatus execute() {
		ActionStatus status = node.run();
		if (status == ActionStatus.RUNNING) {
			return status;
		}
		if (status == ActionStatus.SUCCESS) {
			return ActionStatus.FAILURE;
		}
		return ActionStatus.SUCCESS;
	}
}
