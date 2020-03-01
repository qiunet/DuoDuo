package org.qiunet.test.TestCase.login;

import org.qiunet.test.TestCase.base.BaseOnlineTestCase;
import org.qiunet.test.proto.LoginOnlineProto;
import org.qiunet.test.robot.Robot;
import org.qiunet.utils.string.StringUtil;

/**
 * Created by qiunet.
 * 17/12/9
 */
public class TestLoginOnline extends BaseOnlineTestCase<LoginOnlineProto.LoginOnlineRequest> {
	@Override
	protected LoginOnlineProto.LoginOnlineRequest requestBuild(Robot robot) {
		return LoginOnlineProto.LoginOnlineRequest.newBuilder()
				.setHeader(headerBuilder(robot))
				.build();
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
