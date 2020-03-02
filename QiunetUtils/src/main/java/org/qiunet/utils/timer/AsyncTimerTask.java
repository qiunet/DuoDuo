package org.qiunet.utils.timer;

import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.concurrent.ScheduledFuture;

/**
 * 异步的时间调度的一个类
 * Created by qiunet.
 * 18/1/26
 */
public abstract class AsyncTimerTask implements Runnable {
	private static final Logger logger = LoggerType.DUODUO.getLogger();
	private ScheduledFuture<Void> future;

	void setFuture(ScheduledFuture<Void> future) {
		this.future = future;
	}

	/***
	 * 终止当前线程
	 */
	public void cancel(){
		future.cancel(true);
	}

	public ScheduledFuture<Void> getFuture() {
		return future;
	}

	@Override
	public void run() {
		try {
			asyncRun();
		}catch (Exception e) {
			logger.error("AsyncTimerTask: Exception: ", e);
		}
	}

	/***
	 * 异步执行的方法
	 */
	protected abstract void asyncRun();
}
