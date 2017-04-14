package org.qiunet.gametest.robot;

import org.qiunet.gametest.GameTest;
import org.qiunet.gametest.protocol.base.NormalProtocol;

/**
 *  机器人实例
 *  如果有 tcp udp 需求. 需要实现
 *  udpRobot  tcpRobot 接口.
 * @author qiunet
 *         Created on 16/12/14 08:30.
 */
public abstract class BaseRobot implements IRobot,Runnable {
	private GameTest gameTest;
	@Override
	public void run() {
		for (NormalProtocol protocol : gameTest.getProtocols()) {
			gameTest.getHandler(protocol.getProtocolType()).handler(protocol, this);
		}
	}

	@Override
	public void setGameTest(GameTest test) {
		this.gameTest = test;
	}
}
