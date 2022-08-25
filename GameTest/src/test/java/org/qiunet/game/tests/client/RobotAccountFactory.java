package org.qiunet.game.tests.client;

import org.qiunet.game.test.robot.creator.IRobotAccountFactory;
import org.qiunet.utils.id.DefaultIdGenerator;

/***
 *
 *
 * qiunet
 * 2021/8/31 10:33
 **/
public enum RobotAccountFactory implements IRobotAccountFactory {
	instance;

	private static final DefaultIdGenerator idGenerator = new DefaultIdGenerator();

	@Override
	public String newAccount() {
		return "Account_"+idGenerator.makeId();
	}
}
