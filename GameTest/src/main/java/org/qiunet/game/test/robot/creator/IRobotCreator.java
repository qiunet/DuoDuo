package org.qiunet.game.test.robot.creator;

import org.qiunet.game.test.robot.Robot;

/***
 *
 * robot 工厂接口
 *
 * @author qiunet
 * 2021-07-05 11:29
 */
public interface IRobotCreator {
	/**
	 * 创造一个机器人
	 * 创建时候. 把account 设置好.
	 * @return
	 */
	Robot create();
}
