package org.qiunet.utils.timer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.date.DateUtil;
import reactor.core.Disposable;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * DisposableScheduledFuture 契约测试
 */
public class DisposableScheduledFutureTest {

	@AfterEach
	public void tearDown() {
		DateUtil.clearTimeOffset();
	}

	@Test
	public void testAsCancelHandleGetDelayPositiveBeforeFire() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		DisposableScheduledFuture<Object> future = DisposableScheduledFuture.asCancelHandle(800, TimeUnit.MILLISECONDS);

		long delayMs = future.getDelay(TimeUnit.MILLISECONDS);
		Assertions.assertTrue(delayMs > 500, "getDelay should reflect remaining wait, was " + delayMs);
		Assertions.assertFalse(future.isDone());

		Disposable disposable = SchedulerManager.instance.createMonoTask(() -> "ok", 800, TimeUnit.MILLISECONDS)
			.subscribe(v -> {
				future.completeOneShot(v);
				latch.countDown();
			}, ex -> {
				future.completeExceptionally(ex);
				latch.countDown();
			});
		future.bind(disposable);

		Assertions.assertTrue(future.getDelay(TimeUnit.MILLISECONDS) > 400,
			"getDelay still positive before fire, was " + future.getDelay(TimeUnit.MILLISECONDS));

		Assertions.assertTrue(latch.await(2, TimeUnit.SECONDS));
		Assertions.assertTrue(future.isDone());
		Assertions.assertEquals(0, future.getDelay(TimeUnit.MILLISECONDS));
	}

	@Test
	public void testTimerManagerWallClockDelayGetDelayViaCancel() throws InterruptedException {
		// 通过 DFuture.cancel 路径间接验证 asCancelHandle(delay, unit) 已接上
		var df = TimerManager.instance.scheduleWithDelay(() -> "x", 1000, TimeUnit.MILLISECONDS);
		Assertions.assertFalse(df.isDone());
		Assertions.assertFalse(df.isCancelled());
		Assertions.assertTrue(df.cancel(false));
		Assertions.assertTrue(df.isCancelled());
		Thread.sleep(1100);
		// 已取消则不应再完成成功
		Assertions.assertTrue(df.isCancelled());
	}
}
