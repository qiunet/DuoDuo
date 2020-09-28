package org.qiunet.utils.timer;


import org.qiunet.utils.async.factory.DefaultThreadFactory;
import org.qiunet.utils.async.future.DCompletePromise;
import org.qiunet.utils.async.future.DFuture;
import org.qiunet.utils.date.DateUtil;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.timer.executor.DScheduledThreadPoolExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 *
 * Created by qiunet.
 * 18/1/26
 */
public class TimerManager {
	private static final DScheduledThreadPoolExecutor schedule = new DScheduledThreadPoolExecutor(4, 1000, new DefaultThreadFactory("Qiunet-TimerManager"));

	private static TimerManager instance;

	private TimerManager() {
		if (instance != null) throw new CustomException("Instance Duplication!");
		instance = this;
	}

	public static TimerManager getInstance() {
		if (instance == null) {
			synchronized (TimerManager.class) {
				if (instance == null)
				{
					new TimerManager();
				}
			}
		}
		return instance;
	}
	/***
	 * 停闭
	 */
	public void shutdown(){
		schedule.shutdown();
	}
	/**
	 * 立刻执行
	 * @param callable
	 * @param <V>
	 * @return
	 */
	public <V> DFuture<V> executorNow(Callable<V> callable) {
		DCompletePromise<V> future = new DCompletePromise<>();
		Future<V> submit = schedule.submit(() -> {
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

		ScheduledFuture<T> future = TimerManager.schedule.schedule(caller, delay, unit);
		promise.setFuture(future);
		return promise;
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
