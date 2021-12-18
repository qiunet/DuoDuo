package org.qiunet.game.tests;

import com.google.common.collect.Sets;
import org.qiunet.game.test.response.IStatusTipsHandler;
import org.qiunet.game.tests.client.RobotCreator;
import org.qiunet.game.tests.client.anno.StatusTipsHandler;
import org.qiunet.game.tests.server.ServerStartup;
import org.qiunet.game.tests.server.enums.GameStatus;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.Function;

/***
 *
 *
 * qiunet
 * 2021/7/26 17:21
 **/
public class GameTest {

	public static void main(String[] args) {
		Function<Method, Set<Integer>> mapping = mtd -> {
			StatusTipsHandler annotation = mtd.getAnnotation(StatusTipsHandler.class);
			if (annotation == null) {
				return null;
			}
			Set<Integer> set = Sets.newHashSet();
			for (GameStatus status : annotation.value()) {
				set.add(status.getStatus());
			}
			return set;
		};

		// 启动服务器
		ClassScanner.getInstance(ScannerType.GAME_TEST).addParam(IStatusTipsHandler.STATUS_MAPPING_HANDLER, mapping).scanner();
		ServerStartup server = new ServerStartup();
		server.startup();

		// 需要几个. 就create几个.
		RobotCreator.instance.create();
	}
}
