package org.qiunet.utils.timer;

import org.qiunet.utils.date.DateUtil;
import org.qiunet.utils.logger.LoggerType;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by qiunet.
 * 18/1/27
 */
public class TimerTest {
	private static int time;
	private static ScheduledFuture<?> scheduledFuture;

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		final long createTime = System.currentTimeMillis();

		scheduledFuture = TimerManager.instance.scheduleAtFixedRate(() -> {
			time ++;
			long diff = (System.currentTimeMillis() - createTime);
			LoggerType.DUODUO.info("curr[" + diff + "] time: " + time);
			if (time == 2) {
				try {
					// 执行太久 .会阻塞后面的调度执行的
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			if (time == 5) {
				scheduledFuture.cancel(true);
			}
		}, 0, 1000, TimeUnit.MILLISECONDS);


		Future<String> future = TimerManager.instance.scheduleWithDelay(() -> {
			LoggerType.DUODUO.info("========"+DateUtil.dateToString(DateUtil.currentTimeMillis()));
			Thread.sleep(5000);
			return "SUCCESS";
		}, 1, TimeUnit.SECONDS);

		LoggerType.DUODUO.info(future.get()+ "========"+DateUtil.dateToString(DateUtil.currentTimeMillis()));
	}
}
