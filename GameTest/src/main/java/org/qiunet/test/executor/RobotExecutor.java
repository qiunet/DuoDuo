package org.qiunet.test.executor;


import org.qiunet.test.robot.init.IRobotFactory;
import org.qiunet.test.testcase.ITestCase;
import org.qiunet.utils.async.factory.DefaultThreadFactory;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by qiunet.
 * 17/11/24
 */
public final class RobotExecutor {
	private Logger logger = LoggerType.DUODUO.getLogger();

	private IRobotFactory robotFactory;
	private IExecutorInitializer initializer;
	private List<Class<? extends ITestCase>> testCases = new ArrayList<>(128);

	/**
	 * 测试所有
	 */
	public void testing(){
		this.pressureTesting(1);
	}

	public static RobotExecutor custom(IRobotFactory robotFactory) {
		return new RobotExecutor(robotFactory);
	}

	private RobotExecutor(IRobotFactory robotFactory){
		this.robotFactory = robotFactory;
	}

	private void init() throws Throwable {
		if (initializer == null) return;

		logger.error("-------用户自定义初始化代码开始-------");
		initializer.handler();
		logger.error("-------用户自定义初始化代码结束-------");
	}
	/***
	 * 压测所有
	 * @param robotCount
	 */
	public void pressureTesting(int robotCount) {
		if (robotCount < 1) throw new IllegalArgumentException("robot count can not less than 1! ");

		try {
			this.init();
		} catch (Throwable throwable) {
			logger.error("初始化异常: ", throwable);
			return;
		}

		ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 300, 10 , TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>() , new DefaultThreadFactory("Pressure_Testing_Thread_"));
		logger.info("===============压测开始===============");
		for (int i = 0; i < robotCount; i++) {
			executor.submit(robotFactory.createRobot(testCases));
		}
		while (executor.getActiveCount() != 0 || !executor.getQueue().isEmpty()) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		logger.info("===============压测结束===============");
		executor.shutdown();
	}

	public RobotExecutor setInitializer(IExecutorInitializer initializer) {
		this.initializer = initializer;
		return this;
	}

	public RobotExecutor addTestCase(Class<? extends ITestCase> testCase) {
		this.testCases.add(testCase);
		return this;
	}
}
