package org.qiunet.test.robot;

import org.qiunet.test.robot.init.IRobotInitInfo;
import org.qiunet.test.testcase.ITestCase;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.List;

/**
 * Created by qiunet.
 * 17/12/6
 */
public abstract class AbstractRobot< Info extends IRobotInitInfo> extends BaseRobotFunc<Info> implements IRobot<Info> {
	protected Logger logger = LoggerType.DUODUO_GAME_TEST.getLogger();
	private String brokeReason;

	protected int uid;
	/***游戏的token */
	protected String token;
	/***平台的信息*/
	protected Info info;

	private List<Class<? extends ITestCase>> testCases;

	public AbstractRobot(List<Class<? extends ITestCase>> testCases, Info info) {
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
	public String getToken() {
		return token;
	}

	@Override
	public void setUidAndToken(int uid, String token) {
		this.uid = uid;
		this.token = token;
	}

	@Override
	public void brokeRobot(String brokeReason) {
		this.brokeReason = brokeReason;
	}

	@Override
	public boolean runCases() {
		boolean result = true;
		for (Class<? extends ITestCase> testCaseClass : testCases) {
			ITestCase testCase = null;
			try {
				testCase = testCaseClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				logger.error("Instance Error:", e);
			}

			if (brokeReason != null) {
				logger.error("机器人中断错误: {}", brokeReason);
				result = false;
				break;
			}
			boolean conditionJudgePass = false;
			try {
				conditionJudgePass = testCase.conditionJudge(this);
			}catch (Exception e) {
				logger.error("conditionJudgeError: ", e);
			}
			logger.info("OpenId [{}]({}) [{}] TestCase [{}]", info.getOpenId(), uid, (conditionJudgePass?"Running":"Miss.........."), testCase.getClass().getSimpleName());
			if (conditionJudgePass) {
				testCase.sendRequest(this);
			}else if (testCase.cancelIfConditionMiss()){
				logger.info("TestCase[{}]条件不足并且设置了[cancelIfConditionMiss], 机器人终止测试....", testCaseClass.getSimpleName());
				// 如果条件不满足, 就终止的case
				result = false;
				break;
			}
			try {
				// 发送协议间隔N毫秒
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
