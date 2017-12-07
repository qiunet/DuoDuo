package org.qiunet.test.robot.init;

import org.qiunet.test.robot.IRobot;
import org.qiunet.test.testcase.ITestCase;

import java.util.List;

/**
 * Created by qiunet.
 * 17/12/5
 */
public interface IRobotFactory {
	/***
	 * 得到机器人
	 * @return
	 */
	IRobot createRobot(List<ITestCase> testCaseList);
}
