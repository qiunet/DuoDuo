package org.qiunet.test.function.test.id;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.qiunet.function.id.DistributeIdMaker;
import org.qiunet.test.cross.common.redis.RedisDataUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;
import org.slf4j.Logger;

import java.util.concurrent.CountDownLatch;

/***
 *
 * @author qiunet
 * 2023/2/7 18:05
 */
public class TestDistributeIdMake {
	private static final Logger logger = LoggerType.DUODUO.getLogger();
	private static final int totalCount = 100;
	private static final int thread = 5;
	private int id;

	@BeforeAll
	public static void init(){
		ClassScanner.getInstance(ScannerType.FILE_CONFIG).scanner();
	}

	@Test
	public void test() throws InterruptedException {
		//Preconditions.checkArgument(totalCount % thread == 0);
		CountDownLatch latch = new CountDownLatch(totalCount);
		DistributeIdMaker maker = new DistributeIdMaker("Test_", RedisDataUtil.getInstance(), () -> id++,
				() -> {
					logger.info("update!");
				},
				11);
		for (int i = 0; i < thread; i++) {
			int count = totalCount / thread;
			new Thread(() -> {
				for (int i1 = 0; i1 < count; i1++) {
					maker.generateId();
					latch.countDown();
				}
			}, "thread_"+i).start();
		}

		latch.await();
		Assertions.assertEquals(totalCount, id);
	}
}
