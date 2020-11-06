package org.qiunet.test.executor;


import org.qiunet.test.robot.IRobot;
import org.qiunet.test.robot.init.IRobotFactory;
import org.qiunet.test.testcase.ITestCase;
import org.qiunet.utils.async.future.DFuture;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.timer.TimerManager;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by qiunet.
 * 17/11/24
 */
public final class RobotExecutor {
	private Logger logger = LoggerType.DUODUO_GAME_TEST.getLogger();
	/**已经测试阶段了. 不能再插入新case*/
	private IRobotFactory robotFactory;
	private IExecutorInitializer initializer;
	private AtomicBoolean testing = new AtomicBoolean();
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

		if (robotFactory == null) {
			throw new IllegalStateException("Need robotFactory");
		}

		if (! this.testing.compareAndSet(false, true)){
			logger.error("Current is testing!");
			return;
		}

		try {
			this.init();
		} catch (Throwable throwable) {
			logger.error("初始化异常: ", throwable);
			return;
		}
		CountDownLatch latch = new CountDownLatch(robotCount);
		logger.info("===============压测开始===============");
		for (int i = 0; i < robotCount; i++) {
			DFuture<Boolean> future = TimerManager.executorNow(() -> {
				IRobot robot = robotFactory.createRobot(testCases);
				return robot.runCases();
			});

			future.whenComplete((res, ex) -> latch.countDown());
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			logger.error("压测异常", e);
		}
		logger.info("===============压测结束===============");
	}

	public RobotExecutor setInitializer(IExecutorInitializer initializer) {
		if (testing.get()) {
			throw new IllegalStateException("Already testing");
		}
		this.initializer = initializer;
		return this;
	}

	public RobotExecutor addTestCase(Class<? extends ITestCase> testCase) {
		if (testing.get()) {
			throw new IllegalStateException("Already testing");
		}
		this.testCases.add(testCase);
		return this;
	}
}
