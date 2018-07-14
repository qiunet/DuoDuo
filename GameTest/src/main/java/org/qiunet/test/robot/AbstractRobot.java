package org.qiunet.test.robot;

import org.qiunet.test.robot.init.IRobotInitInfo;
import org.qiunet.test.testcase.ITestCase;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by qiunet.
 * 17/12/6
 */
public abstract class AbstractRobot< Info extends IRobotInitInfo> extends BaseRobotFunc<Info> implements IRobot<Info> {
	protected Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);
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
	public void run() {
		for (Class<? extends ITestCase> testCaseClass : testCases) {
			ITestCase testCase = null;
			try {
				testCase = testCaseClass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			if (brokeReason != null) {
				logger.error("机器人中断错误: "+ brokeReason);
				break;
			}
			boolean conditionJudgePass = false;
			try {
				conditionJudgePass = testCase.conditionJudge(this);
			}catch (Exception e) {
				logger.error("conditionJudgeError: ", e);
			}
			logger.info("OpenId ["+info.getOpenId()+"]("+uid+") ["+(conditionJudgePass?"Running":"Miss..........")+"] TestCase ["+testCase.getClass().getSimpleName()+"]");
			if (conditionJudgePass) {
				testCase.sendRequest(this);
			}else if (testCase.cancelIfConditionMiss()){
				logger.info("TestCase["+testCaseClass.getSimpleName()+"]条件不足并且设置了[cancelIfConditionMiss], 机器人终止测试....");
				// 如果条件不满足, 就终止的case
				break;
			}
		}
	}
}
