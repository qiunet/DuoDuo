package org.qiunet.test.robot;

import org.qiunet.test.robot.init.DefaultRobotInfo;
import org.qiunet.test.testcase.ITestCase;

import java.util.List;

/**
 * Created by qiunet.
 * 17/12/6
 */
public class TestRobot extends AbstractRobot<TestRobot, DefaultRobotInfo> {

	public TestRobot(List<ITestCase> testCases, DefaultRobotInfo info) {
		super(testCases, info);
	}

}
