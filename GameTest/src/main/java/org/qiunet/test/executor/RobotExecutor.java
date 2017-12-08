package org.qiunet.test.executor;


import org.qiunet.flash.handler.gamecfg.GameCfgManagers;
import org.qiunet.test.executor.params.ExecutorParams;
import org.qiunet.test.response.annotation.support.ResponseScannerHandler;
import org.qiunet.utils.classScanner.IScannerHandler;
import org.qiunet.utils.classScanner.ScannerAllClassFile;
import org.qiunet.utils.logger.LoggerManager;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.logger.log.QLogger;

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

	public RobotExecutor(ExecutorParams params){
		this.params = params;
		ScannerAllClassFile scannerAllClassFile = new ScannerAllClassFile();
		logger.error("-------测试初始化开始-------");
		scannerAllClassFile.addScannerHandler(new ResponseScannerHandler());
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

		for (int i = 0; i < robotCount; i++) {
			new Thread(params.getRobotFactory().createRobot(params.getTestCases()), "Pressure_Testing_Thread_"+i).start();
		}
	}
}
