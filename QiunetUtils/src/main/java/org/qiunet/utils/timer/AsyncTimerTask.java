package org.qiunet.utils.timer;

import org.qiunet.utils.nonSyncQuene.mutiThread.MultiNonSyncQueueHandler;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * 异步的时间调度的一个类
 * Created by qiunet.
 * 18/1/26
 */
public abstract class AsyncTimerTask extends TimerTask {
	private static final MultiNonSyncQueueHandler queueHandler = new MultiNonSyncQueueHandler("Qiunet-Timer-MultiNonSyncQueueHandler", 5, 300, 60, TimeUnit.SECONDS);
	@Override
	public void run() {
		queueHandler.addElement(new Runnable() {
			@Override
			public void run() {
				asyncRun();
			}
		});
	}

	protected abstract void asyncRun();
}
