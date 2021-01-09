package org.qiunet.test.response;

import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.test.robot.IRobot;

/**
 * 长连接的响应
 * 
 * Created by qiunet.
 * 17/12/4
 */
public interface IPersistConnResponse<ResponseData, Robot extends IRobot> {
	/***
	 * 响应数据
	 * @param robot
	 * @param content
	 */
	void response(Robot robot, MessageContent content);

	void response(Robot robot, ResponseData data);
}
