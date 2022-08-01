package org.qiunet.game.tests;

import org.qiunet.game.tests.client.RobotCreator;
import org.qiunet.game.tests.server.ServerStartup;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

/***
 *
 *
 * qiunet
 * 2021/7/26 17:21
 **/
public class GameTest {

	public static void main(String[] args) {

		// 启动服务器
		ClassScanner.getInstance(ScannerType.GAME_TEST).scanner();
		ServerStartup server = new ServerStartup();
		server.startup();

		// 需要几个. 就create几个.
		RobotCreator.instance.create();
	}
}
