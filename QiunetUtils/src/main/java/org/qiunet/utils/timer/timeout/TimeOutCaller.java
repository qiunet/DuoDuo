package org.qiunet.utils.timer.timeout;

/***
 * 超时后的回调
 *
 * @author qiunet
 * 2020-10-20 09:56
 */
public interface TimeOutCaller {
	/**
	 * 回调方法
	 * @param future
	 */
	void run(TimeOutFuture future);
}
