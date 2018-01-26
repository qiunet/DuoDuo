package org.qiunet.utils.timer;


import java.util.Date;
import java.util.Timer;

/**
 *
 * Created by qiunet.
 * 18/1/26
 */
public class TimerManager {
	private static final Timer timer = new Timer("Qiunet-TimerManager");

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
	 * 中断timer
	 */
	public void cannel(){
		timer.cancel();
	}

	/**
	 * 清理已经结束cancel的task
	 * @return
	 */
	public int purge(){
		return timer.purge();
	}

	public void scheduleAtFixedRate(AsyncTimerTask timerTask, long deley, long period){
		timer.scheduleAtFixedRate(timerTask, deley, period);
	}

	public void scheduleAtFixedRate(AsyncTimerTask timerTask, Date firstTime, long period){
		timer.scheduleAtFixedRate(timerTask, firstTime, period);
	}

	public void schedule(AsyncTimerTask timerTask, long deley, long period){
		timer.schedule(timerTask, deley, period);
	}

	public void schedule(AsyncTimerTask timerTask, Date firstTime, long period){
		timer.schedule(timerTask, firstTime, period);
	}
}
