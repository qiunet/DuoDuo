package org.qiunet.tests.TestCase.login;

import org.qiunet.tests.TestCase.base.BaseLogicTestCase;
import org.qiunet.tests.proto.LoginRequest;
import org.qiunet.tests.proto.LoginResponse;
import org.qiunet.tests.robot.Robot;

/**
 * Created by qiunet.
 * 17/12/9
 */
public class TestLogin extends BaseLogicTestCase<LoginRequest, LoginResponse> {
	@Override
	protected LoginRequest requestBuild(Robot robot) {
		return LoginRequest.valueOf(robot.getRobotInitInfo().getOpenId()) ;
	}

	@Override
	protected void responseData(Robot robot, LoginResponse loginResponse) {
		robot.setUidAndToken(loginResponse.getUid(), loginResponse.getToken());
	}

	@Override
	public boolean conditionJudge(Robot robot) {
		return true;
	}
}
