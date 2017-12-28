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
public class TestNonSyncQueue extends BaseTest {
	private static NonSyncQueueHandler<QueueElement> testElementNonSyncQueueHandler = NonSyncQueueHandler.create(false);
	@Test
	public void testNonSyncQueue() {
		final AtomicInteger integer = new AtomicInteger(0);
		final int threadCount = 10, loopCount = 10;
		final CountDownLatch latch = new CountDownLatch(threadCount * loopCount);
		for(int i = 0 ; i < threadCount; i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					for (int j = 0 ; j < loopCount; j++) {
						integer.incrementAndGet();
						testElementNonSyncQueueHandler.addElement(new QueueElement(){
							@Override
							public boolean handler() {
								latch.countDown();
								return true;
							}
							@Override
							public String toStr() { return null; }
						});
					}
				}
			}, "-thread-"+i).start();
		}

		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Assert.assertTrue(integer.get() == threadCount * loopCount);
		Assert.assertTrue(testElementNonSyncQueueHandler.size() == 0);
	}
}
