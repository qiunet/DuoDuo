package org.qiunet.utils.timer;


import org.qiunet.utils.nonSyncQuene.factory.DefaultThreadFactory;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * Created by qiunet.
 * 18/1/26
 */
public class TimerManager {
	private static final ScheduledThreadPoolExecutor schedule = new ScheduledThreadPoolExecutor(1, new DefaultThreadFactory("Qiunet-TimerManager"));

	private volatile static TimerManager instance;

	private TimerManager() {
		if (instance != null) throw new RuntimeException("Instance Duplication!");
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
		schedule.shutdownNow();
	}
	/***
	 * 默认使用毫秒
	 * @param timerTask 任务
	 * @param deley 延时毫秒
	 * @param period 调度周期
	 */
	public void scheduleAtFixedRate(AsyncTimerTask timerTask, long deley, long period){
		ScheduledFuture future = schedule.scheduleAtFixedRate(timerTask, deley, period, TimeUnit.MILLISECONDS);
		timerTask.setFuture(future);
	}
}
