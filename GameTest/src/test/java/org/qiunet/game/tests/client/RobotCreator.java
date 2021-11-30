package org.qiunet.game.tests.client;

import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.test.robot.creator.IRobotCreator;
import org.qiunet.utils.id.DefaultIdGenerator;

/***
 *
 *
 * qiunet
 * 2021/8/31 10:33
 **/
public enum RobotCreator implements IRobotCreator {
	instance;

	private static final DefaultIdGenerator idGenerator = new DefaultIdGenerator();

	@Override
	public Robot create() {
		// 或者读取名单. 或者从数据库读取. 都行
		return new Robot("Account_"+idGenerator.makeId());
	}
}
