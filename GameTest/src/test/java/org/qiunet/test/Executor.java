package org.qiunet.test;

import org.qiunet.flash.handler.common.annotation.support.RequestScannerHandler;
import org.qiunet.test.TestCase.login.TestLogin;
import org.qiunet.test.TestCase.login.TestLoginOnline;
import org.qiunet.test.TestCase.player.TestPlayerIndex;
import org.qiunet.test.TestCase.room.TestLoginRoom;
import org.qiunet.test.executor.RobotExecutor;
import org.qiunet.test.executor.params.ExecutorParams;
import org.qiunet.test.robot.IRobot;
import org.qiunet.test.robot.Robot;
import org.qiunet.test.robot.init.DefaultRobotInfo;
import org.qiunet.test.robot.init.IRobotFactory;
import org.qiunet.test.server.ServerStartup;
import org.qiunet.test.server.ServerUidAndTokenBuilder;
import org.qiunet.test.testcase.ITestCase;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by qiunet.
 * 17/12/9
 */
public class Executor {

	public static void main(String[] args) {
		// 启动服务器
		ServerStartup server = new ServerStartup();
		server.startup();
		new RobotExecutor(ExecutorParams.custom()
				.addScannerHandler(new RequestScannerHandler())
				.setRobotFactory(new RobotFactory())

				.addTestCase(new TestLogin())
//				.addTestCase(new TestLoginOnline())
//				.addTestCase(new TestPlayerIndex())
//				.addTestCase(new TestLoginRoom())

				).testing();
		server.shutdown();
	}

	/***
	 * 机器人生成规则
	 * 可以通过返回固定机器人测试某个账号
	 */
	private static class RobotFactory implements IRobotFactory {
		private AtomicInteger incr = new AtomicInteger(100);
		@Override
		public IRobot createRobot(List<ITestCase> testCaseList) {
			String openId = ServerUidAndTokenBuilder.OPENID_PREFIX+incr.getAndIncrement();

			return new Robot(testCaseList, new DefaultRobotInfo(openId, ""));
		}
	}
}
