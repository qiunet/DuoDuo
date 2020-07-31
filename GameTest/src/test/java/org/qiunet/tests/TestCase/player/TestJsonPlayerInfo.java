package org.qiunet.tests.TestCase.player;

import org.qiunet.flash.handler.context.request.http.json.JsonRequest;
import org.qiunet.flash.handler.context.response.json.JsonResponse;
import org.qiunet.tests.TestCase.base.BaseJsonLogicTestCase;
import org.qiunet.tests.robot.Robot;

/**
 * Created by qiunet.
 * 18/1/30
 */
public class TestJsonPlayerInfo extends BaseJsonLogicTestCase {
	@Override
	protected JsonRequest requestBuild0(Robot robot) {
		return new JsonRequest().addAttribute("token", robot.getToken());
	}

	@Override
	protected void responseData(Robot robot, JsonResponse response) {
		System.out.println("---------------"+response+"---------------");
	}

	@Override
	protected int getRequestID() {
		return 1005;
	}

	@Override
	public boolean conditionJudge(Robot robot) {
		return true;
	}
}
