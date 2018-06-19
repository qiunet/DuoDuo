package org.qiunet.test.robot;

import org.qiunet.flash.handler.netty.client.ILongConnClient;
import org.qiunet.test.robot.data.RobotPackData;
import org.qiunet.test.robot.init.DefaultRobotInfo;
import org.qiunet.test.server.IServer;
import org.qiunet.test.testcase.ITestCase;

import java.util.List;

/**
 * Created by qiunet.
 * 17/12/6
 */
public class Robot extends AbstractRobot<DefaultRobotInfo> {
	public RobotPackData packData = new RobotPackData();
	/**累计天数*/
	public int counterDays;

	public int roomSize;

	public Robot(List<Class<? extends ITestCase>> testCases, DefaultRobotInfo info) {
		super(testCases, info);
	}
}
