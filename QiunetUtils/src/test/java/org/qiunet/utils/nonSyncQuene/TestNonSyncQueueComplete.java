package org.qiunet.utils.nonSyncQuene;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.utils.base.BaseTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 如果10个线程一起. 有点慢
 * @author qiunet
 *
 */
public class TestNonSyncQueueComplete extends BaseTest {
	private static NonSyncQueueHandler<TestElement> testElementNonSyncQueueHandler = NonSyncQueueHandler.create(false);
	@Test
	public void testNonSyncQueue() {
		final AtomicInteger integer = new AtomicInteger(0);
		final int threadCount = 2, loopCount = 10;
		for(int i = 0 ; i < threadCount; i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					for (int j = 0 ; j < loopCount; j++) {
						integer.incrementAndGet();
						TestElement element = new TestElement(j+"");
						testElementNonSyncQueueHandler.addElement(element);
					}
				}
			}, "-thread-"+i).start();
		}
		testElementNonSyncQueueHandler.completeAndShutdown();

		Assert.assertTrue(integer.get() == threadCount * loopCount);
		Assert.assertTrue(testElementNonSyncQueueHandler.size() == 0);
	}
}
