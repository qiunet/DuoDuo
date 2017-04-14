package org.qiunet.utils.nonSyncQuene;


import org.junit.Assert;
import org.junit.Test;
import org.qiunet.utils.base.BaseTest;
import org.qiunet.utils.nonSyncQuene.mutiThread.MultiNonSyncQueueHandler;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/***
 * 多线程异步处理 速度明显很快了
 * @author qiunet
 *
 */
public class TestMutiNonAsyncQueue extends BaseTest{
	@Test
	public void testAsyncQueue() {
		final MultiNonSyncQueueHandler print = new MultiNonSyncQueueHandler("TestPrint");
		final AtomicInteger integer = new AtomicInteger(0);
		int threadCount = 10;
		final int loopCount = 5;
		final CountDownLatch latch = new CountDownLatch(threadCount*loopCount);
		for(int i = 0 ; i < threadCount; i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					for (int j = 0; j < loopCount; j++) {
						print.addElement( new Runnable() {
							@Override
							public void run() {
								integer.incrementAndGet();
								latch.countDown();
							}
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
		Assert.assertTrue(print.size() == 0);
		Assert.assertTrue(integer.get() == loopCount * threadCount);
	}

}
