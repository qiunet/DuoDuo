package org.qiunet.utils.asyncQuene;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.utils.base.BaseTest;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 如果10个线程一起. 有点慢
 * @author qiunet
 *
 */
public class TestAsyncQueueComplete extends BaseTest {
	private static AsyncQueueHandler<TestElement> testElementAsyncQueueHandler = AsyncQueueHandler.create(false);
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
						testElementAsyncQueueHandler.addElement(element);
					}
				}
			}, "-thread-"+i).start();
		}
		testElementAsyncQueueHandler.completeAndShutdown();

		Assert.assertTrue(integer.get() == threadCount * loopCount);
		Assert.assertTrue(testElementAsyncQueueHandler.size() == 0);
	}
}
