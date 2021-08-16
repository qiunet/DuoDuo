package org.qiunet.game.test.behavior.builder;

import org.qiunet.game.test.behavior.node.IBehaviorExecutor;
import org.qiunet.game.test.robot.Robot;

/***
 * 构造一个流程行为.
 * 比如登入 -> (注册|注册) -> 首页
 *
 * qiunet
 * 2021/7/21 09:59
 **/
public interface IBehaviorBuilder {
	/**
	 * 该行为的执行器
	 * @return
	 */
	IBehaviorExecutor buildExecutor(Robot robot);
}
