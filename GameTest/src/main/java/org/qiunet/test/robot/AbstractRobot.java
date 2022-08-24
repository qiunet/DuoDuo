package org.qiunet.test.robot;

import org.qiunet.test.robot.init.IRobotInitInfo;
import org.qiunet.test.testcase.ITestCase;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Created by qiunet.
 * 17/12/6
 */
public abstract class AbstractRobot<Info extends IRobotInitInfo> extends BaseRobotFunc<Info> implements IRobot<Info> {
	protected Logger logger = LoggerType.DUODUO.getLogger();
	private String brokeReason;

	protected int uid;
	/***游戏的token */
	protected String token;
	/***平台的信息*/
	protected Info info;

	private List<Class<? extends ITestCase>> testCases;

	private List<Class<? extends ITestCase>> resTestCases;    //协议处理给返回的

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

	public void addTestCases(List<Class<? extends ITestCase>> resTestCases) {
		if (this.resTestCases == null) {
			this.resTestCases = new ArrayList<>();
		}
		this.resTestCases.addAll(resTestCases);
	}

	public void cleanTestCases() {
		this.resTestCases = null;
	}

	@Override
	public void run() {
//		for (Class<? extends ITestCase> testCaseClass : testCases) {
		for (int i = 0; i < testCases.size(); i++) {
			Class<? extends ITestCase> testCaseClass = testCases.get(i);
			if (sendRequest(testCaseClass)) {
				break;
			}
		}
	}

	/**
	 * 发协议
	 *
	 * @param testCaseClass
	 * @return
	 */
	private boolean sendRequest(Class<? extends ITestCase> testCaseClass) {
		ITestCase testCase = null;
		try {
			testCase = testCaseClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		if (brokeReason != null) {
			logger.error("机器人中断错误: " + brokeReason);
			return true;
		}
		boolean conditionJudgePass = false;
		try {
			conditionJudgePass = testCase.conditionJudge(this);
		} catch (Exception e) {
			logger.error("conditionJudgeError: ", e);
		}
		logger.info("OpenId [" + info.getOpenId() + "](" + uid + ") [" + (conditionJudgePass ? "Running" : "Miss..........") + "] TestCase [" + testCase.getClass().getSimpleName() + "]");
		if (conditionJudgePass) {
			testCase.sendRequest(this);
			while (this.resTestCases != null && !this.resTestCases.isEmpty()) {
				for (Class<? extends ITestCase> resTestCase : this.resTestCases) {
					sendRequest(resTestCase);
				}
			}
		} else if (testCase.cancelIfConditionMiss()) {
			logger.info("TestCase[" + testCaseClass.getSimpleName() + "]条件不足并且设置了[cancelIfConditionMiss], 机器人终止测试....");
			// 如果条件不满足, 就终止的case
			return true;
		}
		return false;
	}
}
