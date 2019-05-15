package org.qiunet.utils.asyncQuene;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author qiunet
 *         Created on 17/3/14 11:37.
 */
public class TestIndexAsyncQueue {
	@Test
	public void testIndexAsyncQueue(){
		final IndexAsyncQueueHandler<IndexElement> indexAsyncQueueHandler = new IndexAsyncQueueHandler("");
		final int threadCount = 5, loopCount = 11;
		final CountDownLatch latch = new CountDownLatch(threadCount * loopCount);
		try {
			for(int i = 0 ; i < threadCount; i++){
				new Thread(() -> {
					for (int j = 0 ; j < loopCount; j++) {
						IndexElement element = new IndexElement(j);
						indexAsyncQueueHandler.addElement(element);
						latch.countDown();
					}
				}, "CommonThread"+i).start();
			}
			latch.await();
		}catch (Exception e) {
			e.printStackTrace();
		}
		indexAsyncQueueHandler.shutdown();
		Assert.assertEquals(0, indexAsyncQueueHandler.size());
	}
}
