package org.qiunet.function.ai.node.decorator;

import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.ai.node.IBehaviorNode;
import org.qiunet.function.ai.node.base.BaseDecorator;
import org.qiunet.utils.string.StringUtil;

/***
 * 使颠倒
 *
 * qiunet
 * 2021/8/16 21:36
 **/
public class InvertNode<Owner extends MessageHandler<Owner>> extends BaseDecorator<Owner> {

	public InvertNode(IBehaviorNode<Owner> node) {
		this(node, "");
	}

	public InvertNode(IBehaviorNode<Owner> node, String name) {
		this(name);
		this.addChild(node);
	}

	public InvertNode(String name) {
		super(StringUtil.isEmpty(name) ? "Invert": name);
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
