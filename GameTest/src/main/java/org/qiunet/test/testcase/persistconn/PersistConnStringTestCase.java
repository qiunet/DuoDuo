package org.qiunet.test.testcase.persistconn;

import io.netty.util.CharsetUtil;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.test.robot.IRobot;

/**
 * 长连接的
 * Created by qiunet.
 * 17/12/8
 */
public abstract class PersistConnStringTestCase<Robot extends IRobot> extends PersistConnTestCase<Robot> {
	@Override
	public MessageContent buildRequest(Robot robot) {
		String requestData = requestBuild(robot);
		return new MessageContent(getRequestID(), requestData.getBytes(CharsetUtil.UTF_8));
	}

	public abstract String requestBuild(Robot robot);
}
