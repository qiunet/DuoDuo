package org.qiunet.test.testcase.http;

import io.netty.util.CharsetUtil;
import org.qiunet.test.robot.IRobot;

/**
 * uriPath 类型的字符串处理test case
 * Created by qiunet.
 * 18/1/30
 */
public abstract class UriPathHttpStringTestCase<Robot extends IRobot> extends BaseUriHttpTestCase<String, String, Robot> {
	@Override
	protected byte[] buildRequest(Robot robot) {
		String request = requestBuild(robot);
		return request.getBytes(CharsetUtil.UTF_8);
	}

	@Override
	protected void responseData(Robot robot, byte[] bytes) {
		this.responseData(robot, new String(bytes, CharsetUtil.UTF_8));
	}
}
