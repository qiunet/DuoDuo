package org.qiunet.test.response;

import io.netty.util.CharsetUtil;
import org.qiunet.flash.handler.common.enums.DataType;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.test.robot.IRobot;

/**
 * Created by qiunet.
 * 17/12/8
 */
public abstract class StringResponse<Robot extends IRobot> implements ILongConnResponse<String, Robot> {
	@Override
	public void response(Robot robot, MessageContent content) {
		this.response(robot, (String) DataType.STRING.parseBytes(content.bytes()));
	}
}
