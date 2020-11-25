package org.qiunet.function.test.rank;

import org.junit.BeforeClass;
import org.junit.Test;
import org.qiunet.function.test.targets.PlayerActor;
import org.qiunet.function.test.targets.event.LevelUpEventData;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

/***
 *
 *
 * @author qiunet
 * 2020-11-25 12:55
 */
public class TestRank {
	@BeforeClass
	public static void init(){
		ClassScanner.getInstance(ScannerType.TESTER).scanner();
	}

	@Test
	public void testCacheRank() throws InterruptedException {
		PlayerActor playerActor = new PlayerActor(100000, "测试用户1");

		playerActor.fireEvent(LevelUpEventData.valueOf(1));
		playerActor.fireEvent(LevelUpEventData.valueOf(10));

		Thread.sleep(2000);
	}
}
