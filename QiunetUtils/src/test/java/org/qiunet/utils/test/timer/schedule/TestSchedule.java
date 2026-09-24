package org.qiunet.utils.test.timer.schedule;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.date.DateUtil;
import org.qiunet.utils.timer.SchedulerManager;
import org.qiunet.utils.timer.TimerManager;
import org.qiunet.utils.timer.executor.DCustomSchedule;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 逻辑时间感知调度 + DateUtil 偏移变化测试
 */
public class TestSchedule {

	private final DCustomSchedule schedule = new DCustomSchedule();

	@AfterEach
	public void tearDown() {
		DateUtil.clearTimeOffset();
		schedule.close();
	}

	@Test
	public void testDelayTask() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		AtomicLong cost = new AtomicLong();
		long start = DateUtil.currentTimeMillis();

		schedule.submitTask(() -> {
			cost.set(DateUtil.currentTimeMillis() - start);
			latch.countDown();
		}, 300, TimeUnit.MILLISECONDS);

		Assertions.assertTrue(latch.await(2, TimeUnit.SECONDS));
		Assertions.assertTrue(cost.get() >= 250, "cost=" + cost.get());
		Assertions.assertTrue(cost.get() < 350, "cost=" + cost.get());
	}

	@Test
	public void testCreateMonoTask() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		Mono<String> mono = schedule.createMonoTask(() -> "ok", 200, TimeUnit.MILLISECONDS);
		mono.subscribe(v -> {
			Assertions.assertEquals("ok", v);
			latch.countDown();
		});
		Assertions.assertTrue(latch.await(2, TimeUnit.SECONDS));
	}

	@Test
	public void testCancelBeforeFire() throws InterruptedException {
		AtomicBoolean executed = new AtomicBoolean();
		Disposable disposable = schedule.submitTask(() -> executed.set(true), 800, TimeUnit.MILLISECONDS);
		Thread.sleep(100);
		disposable.dispose();
		Thread.sleep(900);
		Assertions.assertFalse(executed.get());
	}

	@Test
	public void testTimeForwardJumpFiresEarly() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		AtomicLong cost = new AtomicLong();
		long start = DateUtil.currentTimeMillis();

		// 原计划约 5 秒后执行
		schedule.submitTask(() -> {
			cost.set(DateUtil.currentTimeMillis() - start);
			latch.countDown();
		}, 5, TimeUnit.SECONDS);

		Thread.sleep(100);
		// 逻辑时间快进 6 秒; setTimeOffset 立刻通知调度
		DateUtil.setTimeOffset(6, TimeUnit.SECONDS);

		Assertions.assertTrue(latch.await(2, TimeUnit.SECONDS), "task should fire after time jump");
		Assertions.assertTrue(cost.get() >= 6000, "wall cost=" + cost.get());
	}

	@Test
	public void testDateUtilOffsetNotifiesSchedule() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);

		schedule.submitTask(latch::countDown, 10, TimeUnit.SECONDS);

		Thread.sleep(50);
		DateUtil.setTimeOffset(11, TimeUnit.SECONDS);

		Assertions.assertTrue(latch.await(2, TimeUnit.SECONDS),
			"setTimeOffset should notify schedule and fire due task");
	}

	@Test
	public void testMultipleDueTasksOnJump() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(5);
		AtomicInteger count = new AtomicInteger();

		for (int i = 0; i < 5; i++) {
			schedule.submitTask(() -> {
				count.incrementAndGet();
				latch.countDown();
			}, 3, TimeUnit.SECONDS);
		}

		Thread.sleep(50);
		DateUtil.setTimeOffset(4, TimeUnit.SECONDS);

		Assertions.assertTrue(latch.await(2, TimeUnit.SECONDS));
		Assertions.assertEquals(5, count.get());
	}

	@Test
	public void testTimerManagerInstanceFollowsTimeOffset() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		long start = DateUtil.currentTimeMillis();
		AtomicLong cost = new AtomicLong();

		TimerManager.instance.scheduleWithDelay(() -> {
			cost.set(DateUtil.currentTimeMillis() - start);
			latch.countDown();
			return null;
		}, 5, TimeUnit.SECONDS);

		Thread.sleep(50);
		DateUtil.setTimeOffset(6, TimeUnit.SECONDS);

		Assertions.assertTrue(latch.await(2, TimeUnit.SECONDS), "TimerManager.instance should fire after offset");
		Assertions.assertTrue(cost.get() >= 6000, "wall cost=" + cost.get());
	}

	@Test
	public void testSchedulerManagerInstance() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		SchedulerManager.instance.submitTask(latch::countDown, 200, TimeUnit.MILLISECONDS);
		Assertions.assertTrue(latch.await(2, TimeUnit.SECONDS));
	}

	@Test
	public void testPeriodTask() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(3);
		Disposable disposable = SchedulerManager.executor.submitTask(latch::countDown, 0, 100, TimeUnit.MILLISECONDS);
		try {
			Assertions.assertTrue(latch.await(2, TimeUnit.SECONDS));
		} finally {
			disposable.dispose();
		}
	}

	/**
	 * 先挂一个很晚的任务占住队头唤醒, 再并发插入更早的任务.
	 * 修复前: 晚任务的 stale nextSchedule 可能盖掉早任务的唤醒, 导致早任务等到晚任务点才跑.
	 */
	@Test
	public void testEarlierTaskNotStarvedByLaterTask() throws InterruptedException {
		CountDownLatch earlyLatch = new CountDownLatch(1);
		AtomicLong earlyCost = new AtomicLong();
		long start = DateUtil.currentTimeMillis();

		// 晚任务: 3 秒
		schedule.submitTask(() -> {}, 3, TimeUnit.SECONDS);

		// 并发密集插入更早的任务 (约 400ms)
		int earlyCount = 50;
		CountDownLatch allEarly = new CountDownLatch(earlyCount);
		for (int i = 0; i < earlyCount; i++) {
			schedule.submitTask(() -> {
				if (earlyLatch.getCount() > 0) {
					earlyCost.set(DateUtil.currentTimeMillis() - start);
					earlyLatch.countDown();
				}
				allEarly.countDown();
			}, 400, TimeUnit.MILLISECONDS);
		}

		Assertions.assertTrue(earlyLatch.await(2, TimeUnit.SECONDS), "earliest task should not wait for the 3s task");
		Assertions.assertTrue(earlyCost.get() < 1500,
			"early task should fire near 400ms, not near 3s, cost=" + earlyCost.get());
		Assertions.assertTrue(allEarly.await(2, TimeUnit.SECONDS));
	}

	/**
	 * 并发 offer / cancel, 剩余任务仍应按时执行, 不丢唤醒.
	 */
	@Test
	public void testConcurrentOfferAndCancel() throws InterruptedException {
		int total = 80;
		CountDownLatch done = new CountDownLatch(total / 2);
		AtomicInteger executed = new AtomicInteger();
		List<Disposable> toCancel = new CopyOnWriteArrayList<>();

		for (int i = 0; i < total; i++) {
			final int idx = i;
			Disposable d = schedule.submitTask(() -> {
				executed.incrementAndGet();
				done.countDown();
			}, 300 + (idx % 5) * 20L, TimeUnit.MILLISECONDS);
			if (idx % 2 == 0) {
				toCancel.add(d);
			}
		}

		for (Disposable d : toCancel) {
			d.dispose();
		}

		Assertions.assertTrue(done.await(3, TimeUnit.SECONDS),
			"remaining tasks should still fire, executed=" + executed.get());
		Assertions.assertEquals(total / 2, executed.get());
	}
}
