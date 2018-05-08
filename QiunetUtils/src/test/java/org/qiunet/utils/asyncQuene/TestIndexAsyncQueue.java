package org.qiunet.utils.asyncQuene;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author qiunet
 *         Created on 17/3/14 11:37.
 */
public class TestIndexAsyncQueue {
	public static final int THREAD_COUNT = 2;
	@Test
	public void testIndexNonSyncQueue (){
		final IndexAsyncQueueHandler<IndexElement> indexNonSyncQueueHandler = new IndexAsyncQueueHandler(THREAD_COUNT,false);
		final int threadCount = 10, loopCount = 10;
		final CountDownLatch latch = new CountDownLatch(threadCount * loopCount);
		boolean exception = false;
		try {
			for(int i = 0 ; i < threadCount; i++){
				new Thread(new Runnable() {
					@Override
					public void run() {
						for (int j = 0 ; j < loopCount; j++) {
							IndexElement element = new IndexElement(j);
							indexNonSyncQueueHandler.addElement(element, j);
							latch.countDown();
						}
					}
				}, ""+i).start();
			}
			latch.await();
		}catch (Exception e) {
			e.printStackTrace();
			exception = true;
		}

		Assert.assertFalse(exception);
	}
}
