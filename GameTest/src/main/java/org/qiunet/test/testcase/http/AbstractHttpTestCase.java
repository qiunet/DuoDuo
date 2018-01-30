package org.qiunet.test.testcase.http;

import org.qiunet.test.robot.IRobot;
import org.qiunet.test.server.IServer;
import org.qiunet.test.testcase.ITestCase;

/**
 * Created by qiunet.
 * 18/1/30
 */
abstract class AbstractHttpTestCase<RequestData, ResponseData, Robot extends IRobot> implements ITestCase<Robot> {
	/***
	 * 得到当前的server数据
	 * @return
	 */
	protected abstract IServer getServer();

	@Override
	public boolean cancelIfConditionMiss() {
		return false;
	}
	/**
	 * 下层的requestBuild
	 * @param robot
	 * @return
	 */
	protected abstract RequestData requestBuild(Robot robot);

	/**
	 * 下层的响应
	 * @param robot
	 * @param responseData
	 */
	protected abstract void responseData(Robot robot, ResponseData responseData);
}
