package org.qiunet.game.tests;

import org.qiunet.game.test.server.StressTestingServer;
import org.qiunet.game.tests.server.ServerStartup;

/***
 *
 *
 * qiunet
 * 2021/7/26 17:21
 **/
public class GameTest {

	public static void main(String[] args) {
		ServerStartup server = new ServerStartup();
		server.startup();

		StressTestingServer.scanner("org.qiunet.game").startup();
	}
}
