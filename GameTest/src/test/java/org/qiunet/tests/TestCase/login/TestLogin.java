package org.qiunet.tests.TestCase.login;

import org.qiunet.tests.TestCase.base.BaseLogicTestCase;
import org.qiunet.tests.proto.LoginProto;
import org.qiunet.tests.robot.Robot;

/**
 * Created by qiunet.
 * 17/12/9
 */
public class TestLogin extends BaseLogicTestCase<LoginProto.LoginRequest, LoginProto.LoginResponse> {
	@Override
	protected LoginProto.LoginRequest requestBuild(Robot robot) {
		return LoginProto.LoginRequest.newBuilder()
				.setOpenid(robot.getRobotInitInfo().getOpenId())
				.build();
	}

	@Override
	protected void responseData(Robot robot, LoginProto.LoginResponse loginResponse) {
		robot.setUidAndToken(loginResponse.getUid(), loginResponse.getToken());
	}

	@Override
	public boolean conditionJudge(Robot robot) {
		return true;
	}

	@Override
	public int getRequestID() {
		return 1000;
	}
}
