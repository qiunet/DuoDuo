package org.qiunet.game.tests;

import org.qiunet.game.tests.server.ServerStartup;
import org.qiunet.utils.scanner.ClassScanner;

/***
 *
 *
 * qiunet
 * 2021/7/26 17:21
 **/
public class GameTest {

	public static void main(String[] args) {
		// 启动服务器
		ClassScanner.getInstance().scanner();
		ServerStartup server = new ServerStartup();
		server.startup();
	}
}
