package org.qiunet.test.response;

import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.test.robot.IRobot;

import java.nio.charset.StandardCharsets;

/**
 * Created by qiunet.
 * 17/12/8
 */
public abstract class StringResponse<Robot extends IRobot> implements IPersistConnResponse<String, Robot> {
	@Override
	public void response(Robot robot, MessageContent content) {
		this.response(robot, new String(content.bytes(), StandardCharsets.UTF_8));
	}
}
