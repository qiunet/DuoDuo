package org.qiunet.test.testcase.http;

import io.netty.util.CharsetUtil;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.test.robot.IRobot;

import java.nio.charset.StandardCharsets;

/**
 * 一个使用string 交互的http 测试用例基类
 * Created by qiunet.
 * 17/12/8
 */
public abstract class HttpStringTestCase<Robot extends IRobot> extends BaseHttpTestCase<String ,String , Robot> {
	@Override
	public MessageContent buildRequest(Robot robot) {
		String request = requestBuild(robot);
		MessageContent content = new MessageContent(getRequestID(), request.getBytes(CharsetUtil.UTF_8));
		return content;
	}

	@Override
	public void responseData(Robot robot, MessageContent content) {
		this.responseData(robot, new String(content.bytes(), StandardCharsets.UTF_8));
	}
}
