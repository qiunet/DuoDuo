package org.qiunet.game.tests.client.action.login;

import org.qiunet.game.test.behavior.enums.ActionStatus;
import org.qiunet.game.tests.client.action.base.TestAction;
import org.qiunet.game.tests.client.data.condition.AuthCondition;

/***
 *
 *
 * qiunet
 * 2021/8/16 21:07
 **/
public class PlayerIndexAction extends TestAction {

	public PlayerIndexAction() {
		super(new AuthCondition());
	}

	@Override
	protected ActionStatus execute() {
		return null;
	}
}
