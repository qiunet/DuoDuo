package org.qiunet.test.testcase.LongConn;

import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.netty.client.ILongConnClient;
import org.qiunet.test.robot.IRobot;
import org.qiunet.test.server.IServer;
import org.qiunet.test.testcase.ITestCase;

/**
 * Created by qiunet.
 * 17/12/8
 */
abstract class BaseLongConnTestCase<Robot extends IRobot> implements ITestCase<Robot> {
	/***
	 * 请求id
	 * @return
	 */
	protected abstract int getRequestID();
	/***
	 * 得到一个请求数据
	 * @param robot
	 * @return
	 */
	protected abstract MessageContent buildRequest(Robot robot);
	/***
	 * 得到当前的server数据
	 * @return
	 */
	protected abstract IServer getServer();

	@Override
	public void sendRequest(Robot robot) {
		ILongConnClient connClient = robot.getLongConnClient(getServer());
		connClient.sendMessage(buildRequest(robot));
		// 阻塞当前线程
		if (syncWaitForResponse() != 0) robot.parkForResponseID(syncWaitForResponse());
	}

	@Override
	public boolean cancelIfConditionMiss() {
		return false;
	}

	/**
	 * 阻塞等待某个响应.
	 * 比如登录时候是有必要的.
	 * @return 0 不阻塞  其它会阻塞
	 */
	protected int syncWaitForResponse() {
		return 0;
	}
}
