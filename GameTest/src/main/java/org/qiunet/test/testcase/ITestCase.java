package org.qiunet.test.testcase;

import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.test.robot.IRobot;

/**
 * 测试的用例
 * 确认为单例. 所有robot一个. 不能放置全局数据.
 * Created by qiunet.
 * 17/11/24
 */
public interface ITestCase<Robot extends IRobot> {
	/***
	 * 机器人是否可以进行当前testCase
	 * @param robot
	 * @return true 满足条件  false 不满足
	 */
	boolean conditionJudge(Robot robot);

	/***
	 * 发送请求
	 * @param robot
	 * @return 请求本身成功与否请求如果错误, 跟业务无关 返回false 成功 true 比如服务器结果不是200
	 */
	void sendRequest(Robot robot);
	/**
	 * 如果条件不满足. 终止测试
	 * @return true 终止
	 */
	boolean cancelIfConditionMiss();
}
