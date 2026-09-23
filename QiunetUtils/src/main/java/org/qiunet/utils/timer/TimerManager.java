package org.qiunet.utils.timer;


import org.qiunet.utils.async.factory.DefaultThreadFactory;
import org.qiunet.utils.async.future.DCompletePromise;
import org.qiunet.utils.async.future.DFuture;
import org.qiunet.utils.date.DateUtil;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.listener.hook.ShutdownHookUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.thread.ThreadPoolManager;
import reactor.core.Disposable;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 定时任务入口.
 * <ul>
 *   <li>{@link #instance}: 跟随 {@link DateUtil} 逻辑时间 / 偏移, 该提前的会提前执行</li>
 *   <li>{@link #executor}: JDK 单调时钟, 不跟随 setTimeOffset</li>
 * </ul>
 *
 * Created by qiunet.
 * 18/1/26
 */
public enum TimerManager {
	/**
	 * 墙钟 / 逻辑时间感知 (Quartz、玩法延迟等请用这个)
	 */
	instance(false),
	/**
	 * JDK 调度池 (基础设施心跳等不需要跟游戏时间偏移的用这个)
	 */
	executor(true);

	private final boolean jdkClock;
	private final ScheduledExecutorService jdkSchedule;

	TimerManager(boolean jdkClock) {
		this.jdkClock = jdkClock;
		if (jdkClock) {
			this.jdkSchedule = new ScheduledThreadPoolExecutor(8,
				new DefaultThreadFactory("qiunet_jdk_schedule_timerManager"));
			ShutdownHookUtil.getInstance().addShutdownHook(this.jdkSchedule::shutdownNow);
		} else {
			this.jdkSchedule = null;
		}
	}

	/**
	 * 立刻执行
	 */
	public static <V> DFuture<V> executorNow(Runnable callable) {
		return executorNow(() -> {
			callable.run();
			return null;
		});
	}

	public static <V> DFuture<V> executorNow(Callable<V> callable) {
		DCompletePromise<V> future = new DCompletePromise<>();
		Future<V> submit = ThreadPoolManager.NORMAL.submit(() -> {
			V result = null;
			try {
				result = callable.call();
				future.trySuccess(result);
			} catch (Throwable e) {
				future.tryFailure(e);
			}
			return result;
		});
		future.setFuture(submit);
		return future;
	}

	/**
	 * 固定频率周期任务
	 */
	public ScheduledFuture<?> scheduleAtFixedRate(IScheduledTask timerTask, long delay, long period, TimeUnit unit) {
		if (jdkClock) {
			return jdkSchedule.scheduleAtFixedRate(timerTask, delay, period, unit);
		}
		AtomicLong nextFire = new AtomicLong();
		DisposableScheduledFuture<Object> future = DisposableScheduledFuture.createPeriodic(nextFire);
		Disposable disposable = SchedulerManager.instance.submitTask(timerTask, delay, period, unit, nextFire);
		future.bind(disposable);
		return future;
	}

	/**
	 * 延迟任务
	 */
	public <T> DFuture<T> scheduleWithDelay(Runnable delayTask, long delay, TimeUnit unit) {
		return scheduleWithDelay(() -> {
			delayTask.run();
			return null;
		}, delay, unit);
	}

	public <T> DFuture<T> scheduleWithDelay(IDelayTask<T> delayTask, long delay, TimeUnit unit) {
		if (jdkClock) {
			return scheduleWithDelayByJdk(delayTask, delay, unit);
		}
		return scheduleWithDelayByWallClock(delayTask, delay, unit);
	}

	private <T> DFuture<T> scheduleWithDelayByJdk(IDelayTask<T> delayTask, long delay, TimeUnit unit) {
		DCompletePromise<T> promise = new DCompletePromise<>();
		Callable<T> caller = () -> {
			try {
				T result = delayTask.call();
				promise.trySuccess(result);
				return result;
			} catch (Exception e) {
				LoggerType.DUODUO.error("DelayTask Exception: ", e);
				promise.tryFailure(e);
			}
			return null;
		};
		ScheduledFuture<T> future = this.jdkSchedule.schedule(caller, delay, unit);
		promise.setFuture(future);
		return promise;
	}

	private <T> DFuture<T> scheduleWithDelayByWallClock(IDelayTask<T> delayTask, long delay, TimeUnit unit) {
		DCompletePromise<T> promise = new DCompletePromise<>();
		Disposable disposable = SchedulerManager.instance.createMonoTask(delayTask::call, delay, unit)
			.subscribe(promise::trySuccess, ex -> {
				LoggerType.DUODUO.error("DelayTask Exception: ", ex);
				promise.tryFailure(ex);
			});
		@SuppressWarnings({"unchecked", "rawtypes"})
		Future<T> cancelHandle = (Future) DisposableScheduledFuture.asCancelHandle(disposable);
		promise.setFuture(cancelHandle);
		return promise;
	}

	public <T> DFuture<T> scheduleWithTimeMillis(Runnable delayTask, long timeMillis) {
		return scheduleWithTimeMillis(() -> {
			delayTask.run();
			return null;
		}, timeMillis);
	}

	/**
	 * 在指定逻辑时间点执行任务
	 */
	public <T> DFuture<T> scheduleWithTimeMillis(IDelayTask<T> delayTask, long timeMillis) {
		long now = DateUtil.currentTimeMillis();
		if (now - timeMillis > 500) {
			// 有500误差 避免填写的是delay时间.  如果是当前时间误差内. 直接执行.
			throw new CustomException("timeMillis is less than currentTimeMillis");
		}
		return scheduleWithDelay(delayTask, Math.max(1, timeMillis - now), TimeUnit.MILLISECONDS);
	}
}
