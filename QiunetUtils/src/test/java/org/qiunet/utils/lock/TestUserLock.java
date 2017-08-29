package org.qiunet.utils.lock;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by qiunet.
 * 17/8/29
 */
public class TestUserLock {
	@Test
	public void testLock() throws InterruptedException {
		int maxLockedCount = 3;
		final UserLockManager<String> manager = new UserLockManager<>(maxLockedCount);
		final AtomicInteger fastCount = new AtomicInteger();
		final AtomicInteger handleCount = new AtomicInteger();
		int threadCount = 100;
		final CountDownLatch latch = new CountDownLatch(threadCount);
		for (int i = 0; i < threadCount; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						if (! manager.lock("qiunet")) {
							fastCount.incrementAndGet();
							latch.countDown();
							return;
						}
						handleCount.incrementAndGet();
						Thread.sleep(10);
						latch.countDown();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						manager.releaseLock("qiunet");
					}
				}
			}, String.valueOf(i)).start();
		}
		latch.await();

		Assert.assertTrue(manager.getLockedCount("qiunet") == 0);
		Assert.assertTrue(fastCount.get() == (threadCount - maxLockedCount));
		Assert.assertTrue(handleCount.get() == (maxLockedCount));
	}

}
