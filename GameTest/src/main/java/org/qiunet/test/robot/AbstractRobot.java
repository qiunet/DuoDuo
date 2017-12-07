package org.qiunet.test.robot;

import org.qiunet.test.robot.init.IRobotInitInfo;
import org.qiunet.test.testcase.ITestCase;
import org.qiunet.utils.logger.LoggerManager;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.logger.log.QLogger;

import java.util.List;

/**
 * Created by qiunet.
 * 17/12/6
 */
public abstract class AbstractRobot<Robot extends AbstractRobot, Info extends IRobotInitInfo> implements IRobot<Info> {
	protected QLogger logger = LoggerManager.getLogger(LoggerType.GAME_TEST);

	private String brokeReason;

	protected int uid;
	/***游戏的token */
	protected String token;
	/***平台的信息*/
	protected Info info;

	private List<ITestCase> testCases;

	public AbstractRobot(List<ITestCase> testCases, Info info) {
		this.testCases = testCases;
		this.info = info;
	}

	@Override
	public int getUid() {
		return uid;
	}

	@Override
	public Info getRobotInitInfo() {
		return info;
	}

	@Override
	public void brokeRobot(String brokeReason) {
		this.brokeReason = brokeReason;
	}
	@Override
	public void run() {
		for (ITestCase testCase : testCases) {
			if (brokeReason != null) {
				logger.error("中断错误: "+ brokeReason);
				break;
			}

			boolean conditionJudgePass = testCase.conditionJudge(this);
			logger.info("OpenId ["+info.getOpenId()+"]("+uid+") ["+(conditionJudgePass?"Running":"Miss..........")+"] TestCase ["+testCase.getClass().getSimpleName()+"]");
			if (conditionJudgePass) {
				testCase.sendRequest(this);
			}else if (testCase.cancelIfConditionMiss()){
				logger.info("程序终止....");
				// 如果条件不满足, 就终止的case
				break;
			}

		}
	}
}
