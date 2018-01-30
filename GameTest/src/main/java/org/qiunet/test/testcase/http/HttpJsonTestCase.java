package org.qiunet.test.testcase.http;

import org.qiunet.flash.handler.context.request.http.json.JsonRequest;
import org.qiunet.flash.handler.context.response.json.JsonResponse;
import org.qiunet.test.robot.IRobot;

/**
 * Created by qiunet.
 * 18/1/30
 */
public abstract class HttpJsonTestCase<Robot extends IRobot> extends HttpStringTestCase<Robot> {

	@Override
	protected String requestBuild(Robot robot) {
		JsonRequest request = requestBuild0(robot);
		return request.toString();
	}

	protected abstract JsonRequest requestBuild0(Robot robot);

	@Override
	protected void responseData(Robot robot, String s) {
		JsonResponse response = JsonResponse.parse(s);
		this.responseData(robot, response);
	}

	protected abstract void responseData(Robot robot, JsonResponse response);
}
