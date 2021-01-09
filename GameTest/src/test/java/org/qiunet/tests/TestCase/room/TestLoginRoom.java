package org.qiunet.tests.TestCase.room;

import org.qiunet.tests.TestCase.base.PersistRoomTestCase;
import org.qiunet.tests.proto.LoginRoomRequest;
import org.qiunet.tests.robot.Robot;
import org.qiunet.utils.string.StringUtil;

/**
 * Created by qiunet.
 * 17/12/9
 */
public class TestLoginRoom extends PersistRoomTestCase<LoginRoomRequest> {
	@Override
	protected LoginRoomRequest requestBuild(Robot robot) {
		return new LoginRoomRequest();
	}

	@Override
	public boolean conditionJudge(Robot robot) {
		return !StringUtil.isEmpty(robot.getToken()) && robot.getUid() != 0;
	}

	@Override
	protected int syncWaitForResponse() {
		return 1000003;
	}

	@Override
	public int getRequestID() {
		return 1003;
	}
}
