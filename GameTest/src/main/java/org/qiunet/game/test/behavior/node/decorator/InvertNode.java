package org.qiunet.game.test.behavior.node.decorator;

import org.qiunet.game.test.behavior.enums.ActionStatus;
import org.qiunet.game.test.behavior.node.IBehaviorAction;
import org.qiunet.game.test.behavior.node.base.BaseDecorator;

/***
 * 使颠倒
 *
 * qiunet
 * 2021/8/16 21:36
 **/
public class InvertNode extends BaseDecorator {

	public InvertNode(IBehaviorAction action) {
		super(action);
	}

	@Override
	protected ActionStatus execute() {
		return action.run();
	}
}
