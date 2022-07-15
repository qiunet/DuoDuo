package org.qiunet.utils.timer;


import org.qiunet.utils.async.factory.DefaultThreadFactory;
import org.qiunet.utils.async.future.DCompletePromise;
import org.qiunet.utils.async.future.DFuture;
import org.qiunet.utils.date.DateUtil;
import org.qiunet.utils.listener.event.EventListener;
import org.qiunet.utils.listener.event.data.ServerShutdownEventData;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.thread.ThreadPoolManager;
import org.qiunet.utils.timer.executor.DScheduledThreadPoolExecutor;

import java.util.concurrent.*;

/**
 *
 * Created by qiunet.
 * 18/1/26
 */
public enum TimerManager {
	/**
	 * 自定义的 ScheduledThreadPool
	 * 需要调时间有效的, 使用该实例
	 */
	instance(new DScheduledThreadPoolExecutor(8, 1000, new DefaultThreadFactory("qiunet_fix_schedule_timerManager"))),
	/**
	 * 系统自带的 ScheduledThreadPool

	 */
	executor(new ScheduledThreadPoolExecutor(8, new DefaultThreadFactory("qiunet_jdk_schedule_timerManager"))),
	;

	private final ScheduledExecutorService schedule;
	TimerManager(ScheduledExecutorService executorService) {
		this.schedule = executorService;
	}

	@EventListener
	private void shutdown(ServerShutdownEventData eventData) {
		for (TimerManager value : values()) {
			value.schedule.shutdown();
		}
	}

	/**
	 * 立刻执行
	 * @param callable
	 * @param <V>
	 * @return
	 */
	public static  <V> DFuture<V> executorNow(Runnable callable) {
		return executorNow(() -> {
			callable.run();
			return null;
		});
	}

	public static  <V> DFuture<V> executorNow(Callable<V> callable) {
		DCompletePromise<V> future = new DCompletePromise<>();
		Future<V> submit = ThreadPoolManager.NORMAL.submit(() -> {
			V result = null;
			try {
				result = callable.call();
				future.trySuccess(result);
			} catch (Exception e) {
				future.tryFailure(e);
			}
			return result;
		});
		future.setFuture(submit);
		return future;
	}
	/***
	 * 默认使用毫秒
	 * @param timerTask 任务
	 * @param delay 延时毫秒
	 * @param period 调度周期
	 */
	public ScheduledFuture<?> scheduleAtFixedRate(IScheduledTask timerTask, long delay, long period, TimeUnit unit){
		return schedule.scheduleAtFixedRate(timerTask, delay, period, unit);
	}

	/***
	 * 添加延迟处理任务.
	 * @param delayTask 任务
	 * @param delay 延迟参数
	 * @param unit 时间格式
	 * @param <T>
	 */
	public <T> DFuture<T> scheduleWithDelay(Runnable delayTask, long delay, TimeUnit unit) {
		return scheduleWithDelay(() -> {
			delayTask.run();
			return null;
		}, delay, unit);
	}

	public <T> DFuture<T> scheduleWithDelay(IDelayTask<T> delayTask, long delay, TimeUnit unit) {
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

		ScheduledFuture<T> future = this.schedule.schedule(caller, delay, unit);
		promise.setFuture(future);
		return promise;
	}

	public <T> DFuture<T> scheduleWithTimeMillis(Runnable delayTask, long timeMillis) {
		return scheduleWithTimeMillis(() -> {
			delayTask.run();
			return null;
		}, timeMillis);
	}
	/***
	 * 在一个指定的时间点执行任务
	 * @param delayTask
	 * @param timeMillis
	 * @param <T>
	 * @return
	 */

	public <T> DFuture<T> scheduleWithTimeMillis(IDelayTask<T> delayTask, long timeMillis) {
		long now = DateUtil.currentTimeMillis();
		if (timeMillis < now) {
			throw new IllegalArgumentException("timeMillis is less than currentTimeMillis");
		}
		return scheduleWithDelay(delayTask, (timeMillis - now), TimeUnit.MILLISECONDS);
	}
}
