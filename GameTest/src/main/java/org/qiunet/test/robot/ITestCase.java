package org.qiunet.test.robot;

/**
 * Created by qiunet.
 * 17/11/24
 */
public interface ITestCase {
	/***
	 * 机器人是否可以进行当前testCase
	 * @param robot
	 * @return true 满足条件  false 不满足
	 */
	boolean conditionJudge(IRobot robot);

	/**
	 * 如果条件不满足. 终止测试
	 * @return true 终止
	 */
	boolean cancelIfConditionMiss();
}
