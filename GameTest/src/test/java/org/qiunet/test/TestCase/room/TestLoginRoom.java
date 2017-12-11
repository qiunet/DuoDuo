package org.qiunet.test.TestCase.room;

import org.qiunet.test.TestCase.BaseRoomTestCase;
import org.qiunet.test.proto.LoginRoomProto;
import org.qiunet.test.robot.Robot;
import org.qiunet.utils.string.StringUtil;

/**
 * Created by qiunet.
 * 17/12/9
 */
public class TestLoginRoom extends BaseRoomTestCase<LoginRoomProto.LoginRoomRequest> {
	@Override
	protected LoginRoomProto.LoginRoomRequest requestBuild(Robot robot) {
		return LoginRoomProto.LoginRoomRequest.newBuilder().setHeader(headerBuilder(robot)).build();
	}

	@Override
	public boolean conditionJudge(Robot robot) {
		if (StringUtil.isEmpty(robot.getToken()) || robot.getUid() == 0) return false;
		return true;
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
