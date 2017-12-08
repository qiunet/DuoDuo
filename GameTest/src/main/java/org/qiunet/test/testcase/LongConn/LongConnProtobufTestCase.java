package org.qiunet.test.testcase.LongConn;

import com.google.protobuf.GeneratedMessageV3;
import org.qiunet.flash.handler.context.header.MessageContent;
import org.qiunet.test.robot.IRobot;

/**
 * Created by qiunet.
 * 17/12/8
 */
public abstract class LongConnProtobufTestCase<RequestData extends GeneratedMessageV3, Robot extends IRobot> extends BaseLongConnTestCase<Robot> {
	@Override
	public MessageContent buildRequest(Robot robot) {
		RequestData requestData = requestBuild(robot);
		MessageContent content = new MessageContent(getRequestID(), requestData.toByteArray());
		return content;
	}

	protected abstract RequestData requestBuild(Robot robot);
}
