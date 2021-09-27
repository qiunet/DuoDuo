package org.qiunet.game.test.robot.creator;

import org.qiunet.game.test.robot.Robot;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

	/**
	 * 创建一定数量的机器人
	 * @param count 数量
	 * @return 机器人的列表
	 */
	default List<Robot> create(int count) {
		return IntStream.range(0, count).mapToObj(i -> create()).collect(Collectors.toList());
	}
}
