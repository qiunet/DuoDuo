package org.qiunet.test.function.test.monitor;

import org.qiunet.function.monitor.DefaultMonitor;
import org.qiunet.function.monitor.IMonitor;
import org.qiunet.utils.date.DateUtil;

import java.util.concurrent.TimeUnit;

/***
 *
 * @author qiunet
 * 2020-03-21 18:05
 **/
public class TestMonitor {
	private final long curr = System.currentTimeMillis();

	private enum LogType {
		SHOP_BUY(1), MISSION(5), TASK(10);
		private final int count;
		LogType(int count) {
			this.count = count;
		}

		public int getCount() {
			return count;
		}
	}

	private final IMonitor<Long, LogType> monitor = new DefaultMonitor<>(
											LogType::getCount,
												(data) -> {
													System.out.println((DateUtil.currentTimeMillis() - curr)+ "num ["+data.triggerNum()+"] delayTimes ["+data.delayTimes()+"]");
													return false;
												},
											LogType.SHOP_BUY.getCount(),
											TimeUnit.SECONDS

		);


	public void test(){
		long uid = 10000; int sleepMillis = 0;
		long millis = TimeUnit.SECONDS.toMillis(LogType.SHOP_BUY.getCount());
		for (int i = 0; i < 12; i++) {
			monitor.add(uid, LogType.SHOP_BUY);

			long sleep = millis - 10;
			if (i == 7) sleep = 6 * millis;
			sleepMillis += sleep;
			DateUtil.setTimeOffset(sleepMillis, TimeUnit.MILLISECONDS);
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		TestMonitor monitor = new TestMonitor();
		monitor.test();
	}
}
