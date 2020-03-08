package org.qiunet.test.executor;


import org.qiunet.test.executor.params.ExecutorParams;
import org.qiunet.utils.async.factory.DefaultThreadFactory;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by qiunet.
 * 17/11/24
 */
public final class RobotExecutor {
	private Logger logger = LoggerType.DUODUO.getLogger();
	private ExecutorParams params;
	/**
	 * 测试所有
	 */
	public void testing(){
		this.pressureTesting(1);
	}

	public RobotExecutor(ExecutorParams.Builder builder){
		this(builder.build());
	}

	public RobotExecutor(ExecutorParams params){
		this.params = params;
		// 已经默认集成了 org.qiunet
//		logger.error("-------测试初始化开始-------");
//		ClassScanner.getInstance().scanner();
//		logger.error("-------测试初始化结束-------");

		if (params.getInitializer() != null) {
			logger.error("-------用户自定义初始化代码开始-------");
			params.getInitializer().handler();
			logger.error("-------用户自定义初始化代码结束-------");
		}
	}
	/***
	 * 压测所有
	 * @param robotCount
	 */
	public void pressureTesting(int robotCount) {
		if (robotCount < 1) throw new IllegalArgumentException("robot count can not less than 1! ");
		ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 300, 10 , TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>() , new DefaultThreadFactory("Pressure_Testing_Thread_"));
		logger.info("===============压测开始===============");
		for (int i = 0; i < robotCount; i++) {
			executor.submit(params.getRobotFactory().createRobot(params.getTestCases()));
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
}
