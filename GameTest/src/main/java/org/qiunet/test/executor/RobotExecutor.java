package org.qiunet.test.executor;


import org.qiunet.flash.handler.gamecfg.GameCfgManagers;
import org.qiunet.flash.handler.netty.client.http.NettyHttpClient;
import org.qiunet.test.executor.params.ExecutorParams;
import org.qiunet.utils.classScanner.IScannerHandler;
import org.qiunet.utils.classScanner.ScannerAllClassFile;
import org.qiunet.utils.logger.LoggerManager;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.logger.log.QLogger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by qiunet.
 * 17/11/24
 */
public final class RobotExecutor {
	private QLogger logger = LoggerManager.getLogger(LoggerType.GAME_TEST);
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
		ScannerAllClassFile scannerAllClassFile = new ScannerAllClassFile();
		logger.error("-------测试初始化开始-------");
		for (IScannerHandler scannerHandler : params.getScannerHandlers()) {
			scannerAllClassFile.addScannerHandler(scannerHandler);
		}
		try {
			scannerAllClassFile.scanner();
			GameCfgManagers.getInstance().initSetting();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.error("-------测试初始化结束-------");

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

		Map<String, Thread> allThread = new HashMap<>();

		logger.info("===============压测开始===============");
		for (int i = 0; i < robotCount; i++) {
			String threadName = "Pressure_Testing_Thread_"+i;
			Thread thread = new Thread(params.getRobotFactory().createRobot(params.getTestCases()), threadName);
			thread.start();

			allThread.put(threadName, thread);
		}
		/***阻断  直到所有的thread都结束*/
		do {
			Iterator<Map.Entry<String,Thread>> it = allThread.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String,Thread> en = it.next();
				if (! en.getValue().isAlive()) it.remove();
			}

			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (! allThread.isEmpty());
		logger.info("===============压测结束===============");
		NettyHttpClient.shutdown();
	}
}
