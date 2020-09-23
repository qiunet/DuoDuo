package org.qiunet.tests.TestCase.login;

import org.qiunet.tests.TestCase.base.BaseOnlineTestCase;
import org.qiunet.tests.proto.LoginOnlineRequest;
import org.qiunet.tests.robot.Robot;
import org.qiunet.utils.string.StringUtil;

/**
 * Created by qiunet.
 * 17/12/9
 */
public class TestLoginOnline extends BaseOnlineTestCase<LoginOnlineRequest> {
	@Override
	protected LoginOnlineRequest requestBuild(Robot robot) {
		return new LoginOnlineRequest();
	}

	@Override
	public boolean conditionJudge(Robot robot) {
		return !StringUtil.isEmpty(robot.getToken()) && robot.getUid() != 0;
	}

	@Override
	public boolean cancelIfConditionMiss() {
		return true;
	}

	@Override
	protected int syncWaitForResponse() {
		return 1000000;
	}

	@Override
	public int getRequestID() {
		return 1001;
	}
}
