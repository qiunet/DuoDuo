package org.qiunet.utils.timer.timeout;

import org.qiunet.utils.timer.TimerManager;

import java.util.concurrent.TimeUnit;

/***
 * 负责timeout的提供 回调
 * HashedWheelTimer 也有类似的功能.
 *
 * @author qiunet
 * 2020-10-20 09:45
 */
public final class Timeout {

	/**
	 * 几秒后超时
	 * @param caller  超时回调方法
	 * @param seconds 秒
	 * @return
	 */
	public static TimeOutFuture newTimeOut(TimeOutCaller caller, int seconds) {
		return newTimeOut(caller, seconds, TimeUnit.SECONDS);
	}

	/**
	 * 指定时间单位的超时
	 * @param caller 超时回调方法
	 * @param timeoutTime 时间数
	 * @param unit 时间单位
	 * @return
	 */
	public static TimeOutFuture newTimeOut(TimeOutCaller caller, int timeoutTime, TimeUnit unit) {
		TimeOutFuture future = new TimeOutFuture(caller);
		future.dFuture = TimerManager.executor.scheduleWithDelay(() -> {
			future.timeout();
			return null;
		}, timeoutTime, unit);
		return future;
	}
}
