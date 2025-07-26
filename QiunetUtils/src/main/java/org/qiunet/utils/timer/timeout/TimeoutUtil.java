package org.qiunet.utils.timer.timeout;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.qiunet.utils.async.factory.DefaultThreadFactory;

import java.util.concurrent.TimeUnit;

/***
 * 负责timeout的提供 回调
 *
 * @author qiunet
 * 2020-10-20 09:45
 */
public final class TimeoutUtil {
	private static final HashedWheelTimer timer = new HashedWheelTimer(new DefaultThreadFactory("TimeoutUtil"));
	/**
	 * 几秒后超时
	 * @param caller  超时回调方法
	 * @param seconds 秒
	 * @return
	 */
	public static Timeout  newTimeOut(TimerTask caller, int seconds) {
		return newTimeOut(caller, seconds, TimeUnit.SECONDS);
	}

	/**
	 * 指定时间单位的超时
	 * @param caller 超时回调方法
	 * @param timeoutTime 时间数
	 * @param unit 时间单位
	 * @return
	 */
	public static Timeout newTimeOut(TimerTask caller, int timeoutTime, TimeUnit unit) {
		return timer.newTimeout(caller, timeoutTime, unit);
	}
}
