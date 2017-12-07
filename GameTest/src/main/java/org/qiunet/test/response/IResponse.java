package org.qiunet.test.response;

import org.qiunet.flash.handler.context.header.MessageContent;
import org.qiunet.test.robot.IRobot;

/**
 * Created by qiunet.
 * 17/12/4
 */
public interface IResponse {
	/***
	 * 响应数据
	 * @param robot
	 * @param content
	 */
	void response(IRobot robot, MessageContent content);
}
