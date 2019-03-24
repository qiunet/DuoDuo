package org.qiunet.utils.timer;

import org.qiunet.utils.date.DateUtil;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by qiunet.
 * 18/1/27
 */
public class TimerTest {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		final long createTime = System.currentTimeMillis();

		TimerManager.getInstance().scheduleAtFixedRate(new AsyncTimerTask() {
			private int time;
			@Override
			protected void asyncRun() {
				long diff = (System.currentTimeMillis() - createTime);
				System.out.println("time["+time+"] diff: "+diff);
				if (diff < (1000 * (time+1))){
					return;
				}
				time = (int) (diff / 1000);
				System.out.println("curr["+diff+"] Seconds: " + Math.round(diff / 1000f));
				if (time == 2) {
					try {
						Thread.sleep(800);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				if (time == 5) {
					this.cancel();
				}
			}
		}, 0, 100);


		Future<String> future = TimerManager.getInstance().scheduleWithDeley(new DelayTask<String>() {
			@Override
			public String call() throws Exception {
				System.out.println("========"+DateUtil.dateToString(new Date()));
				Thread.sleep(5000);
				return "SUCCESS";
			}
		}, 1, TimeUnit.SECONDS);

		System.out.println(future.get()+ "========"+DateUtil.dateToString(new Date()));
	}
}
