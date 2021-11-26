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
public class InvertNode extends BaseDecorator {

	public InvertNode(IBehaviorNode node) {
		super(node);
	}

	@Override
	protected ActionStatus execute() {
		return node.run();
	}
}
