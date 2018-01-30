package org.qiunet.test.TestCase.player;

import org.qiunet.test.TestCase.base.BaseStringLogicTestCase;
import org.qiunet.test.robot.Robot;

/**
 * Created by qiunet.
 * 18/1/30
 */
public class TestPlayerInfo extends BaseStringLogicTestCase {
	@Override
	protected int getRequestID() {
		return 1004;
	}

	@Override
	protected String requestBuild(Robot robot) {
		return robot.getToken();
	}

	@Override
	protected void responseData(Robot robot, String s) {
		System.out.println("=============="+s+"============");
	}

	@Override
	public boolean conditionJudge(Robot robot) {
		return true;
	}
}
