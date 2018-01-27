package org.qiunet.utils.timer;

import org.qiunet.utils.logger.LoggerManager;
import org.qiunet.utils.logger.log.QLogger;

import java.util.concurrent.ScheduledFuture;

/**
 * 异步的时间调度的一个类
 * Created by qiunet.
 * 18/1/26
 */
public abstract class AsyncTimerTask implements Runnable {
	private static final QLogger logger = LoggerManager.getLogger(AsyncTimerTask.class);
	private ScheduledFuture future;

	void setFuture(ScheduledFuture future) {
		this.future = future;
	}

	/***
	 * 终止当前线程
	 */
	public void cancel(){
		future.cancel(true);
	}

	@Override
	public void run() {
		try {
			asyncRun();
		}catch (Exception e) {
			logger.error("AsyncTimerTask: Exception: ", e);
		}
	}

	protected abstract void asyncRun();
}
