package org.qiunet.utils.timer.timeout;

import org.qiunet.utils.async.future.DFuture;

import java.util.concurrent.atomic.AtomicReference;

/***
 *
 *
 * @author qiunet
 * 2020-10-20 09:57
 */
public class TimeOutFuture {
	public enum Status {
		/**初始状态*/
		INIT,
		/**取消状态**/
		CANCELED,
		/** 超时状态 */
		TIMEOUT
	}

	private final AtomicReference<Status> status = new AtomicReference<>(Status.INIT);

	private final TimeOutCaller caller;

	DFuture<Void> dFuture;

	TimeOutFuture(TimeOutCaller caller) {
		this.caller = caller;
	}

	/**
	 * 结束超时计时器
	 * @return
	 */
	public boolean cancel(){
		if (! compareAndSetStatus(Status.INIT, Status.CANCELED)) {
			return false;

		}
		return dFuture.cancel(false);
	}

	/**
	 * 获得当前状态
	 * @return
	 */
	public Status status() {
		return status.get();
	}

	/**
	 * 是否已经超时, 处理完毕
	 * @return
	 */
	public boolean isTimeOut(){
		return status() == Status.TIMEOUT;
	}

	/**
	 * 是否已经取消
	 * @return
	 */
	public boolean isCanceled(){
		return status() == Status.CANCELED;
	}

	void timeout(){
		if (! compareAndSetStatus(Status.INIT, Status.TIMEOUT)) {
			return;
		}

		caller.run(this);
	}


	private boolean compareAndSetStatus(Status expect, Status update) {
		return status.compareAndSet(expect, update);
	}
}
