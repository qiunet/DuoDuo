package org.qiunet.test.testcase.http;

import com.google.protobuf.GeneratedMessageV3;
import org.qiunet.flash.handler.context.header.MessageContent;
import org.qiunet.test.robot.IRobot;

/**
 *  一个http protobuf 类型的测试用例
 * Created by qiunet.
 * 17/12/8
 */
public abstract class HttpProtobufTestCase<RequestData extends GeneratedMessageV3, ResponseData extends GeneratedMessageV3, Robot extends IRobot> extends BaseHttpTestCase<RequestData, ResponseData, Robot> {
	@Override
	public MessageContent buildRequest(Robot robot) {
		RequestData requestData = requestBuild(robot);
		byte [] bytes = requestData.toByteArray();
		MessageContent content = new MessageContent(getRequestID() , bytes);
		return content;
	}

	@Override
	public void responseData(Robot robot, MessageContent content) {

	}
}
