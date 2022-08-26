package org.qiunet.game.test.robot.creator;

/***
 *
 * robot 账号提供
 *
 * @author qiunet
 * 2021-07-05 11:29
 */
public interface IRobotAccountFactory {

	/**
	 * 提供account.
	 * 一个robot 初始化时候调用一次.
	 * @return
	 */
	String newAccount();
}
