package org.qiunet.tests.TestCase.player;

import org.qiunet.flash.handler.context.request.http.json.JsonRequest;
import org.qiunet.flash.handler.context.response.json.JsonResponse;
import org.qiunet.tests.TestCase.base.BaseUriPathJsonTestCase;
import org.qiunet.tests.robot.Robot;

/**
 * Created by qiunet.
 * 18/1/30
 */
public class TestUriPathPlayerInfo extends BaseUriPathJsonTestCase {
	@Override
	public String getUriPath() {
		return "playerInfo";
	}

	@Override
	protected JsonRequest requestBuild(Robot robot) {
		JsonRequest request = new JsonRequest();
		request.addAttribute("token", robot.getToken());
		return request;
	}

	@Override
	protected void responseData(Robot robot, JsonResponse response) {
		System.out.println("##############["+response+"]#################");
	}

	@Override
	public boolean conditionJudge(Robot robot) {
		return true;
	}
}
