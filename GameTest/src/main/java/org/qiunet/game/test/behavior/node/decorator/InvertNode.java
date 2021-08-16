package org.qiunet.game.test.behavior.node.decorator;

import org.qiunet.game.test.behavior.enums.ActionStatus;
import org.qiunet.game.test.behavior.node.IBehaviorNode;
import org.qiunet.game.test.behavior.node.base.BaseDecorator;

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
