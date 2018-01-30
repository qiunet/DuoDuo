package org.qiunet.test.testcase.http;

import io.netty.util.CharsetUtil;
import org.qiunet.flash.handler.context.request.http.json.JsonRequest;
import org.qiunet.flash.handler.context.response.json.JsonResponse;
import org.qiunet.test.robot.IRobot;

/**
 * uriPath 类型的json处理test case
 * Created by qiunet.
 * 18/1/30
 */
public abstract class UriPathHttpJsonTestCase<Robot extends IRobot> extends BaseUriHttpTestCase<JsonRequest, JsonResponse, Robot> {
	@Override
	protected byte[] buildRequest(Robot robot) {
		JsonRequest request = requestBuild(robot);
		return request.toString().getBytes(CharsetUtil.UTF_8);
	}

	@Override
	protected void responseData(Robot robot, byte[] bytes) {
		this.responseData(robot, JsonResponse.parse(new String(bytes, CharsetUtil.UTF_8)));
	}
}
