package org.qiunet.test.executor;


import com.google.common.collect.Sets;
import org.qiunet.test.robot.IRobot;
import org.qiunet.test.robot.init.IRobotFactory;
import org.qiunet.test.testcase.ITestCase;
import org.qiunet.utils.async.future.DFuture;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.timer.TimerManager;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by qiunet.
 * 17/11/24
 */
public final class RobotExecutor {
	private Logger logger = LoggerType.DUODUO_GAME_TEST.getLogger();
	/**已经测试阶段了. 不能再插入新case*/
	private boolean testing;
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
	private Set<DFuture<Boolean>> futures = Sets.newConcurrentHashSet();
	private Thread currThread;
	/***
	 * 压测所有
	 * @param robotCount
	 */
	public void pressureTesting(int robotCount) {
		if (robotCount < 1) throw new IllegalArgumentException("robot count can not less than 1! ");

		try {
			this.init();
			this.testing = true;
		} catch (Throwable throwable) {
			logger.error("初始化异常: ", throwable);
			return;
		}

		logger.info("===============压测开始===============");
		currThread = Thread.currentThread();
		for (int i = 0; i < robotCount; i++) {
			DFuture<Boolean> future = TimerManager.executor.executorNow(() -> {
				IRobot robot = robotFactory.createRobot(testCases);
				return robot.runCases();
			});

			future.whenComplete((res, ex) -> futureComplete(future));
			futures.add(future);
		}
		LockSupport.park();
		logger.info("===============压测结束===============");
	}

	private void futureComplete(DFuture<Boolean> future) {
		futures.remove(future);
		if (futures.isEmpty()) {
			LockSupport.unpark(currThread);
		}
	}

	public RobotExecutor setInitializer(IExecutorInitializer initializer) {
		if (testing) {
			throw new IllegalStateException("Already testing");
		}
		this.initializer = initializer;
		return this;
	}

	public RobotExecutor addTestCase(Class<? extends ITestCase> testCase) {
		if (testing) {
			throw new IllegalStateException("Already testing");
		}
		this.testCases.add(testCase);
		return this;
	}
}
