package org.qiunet.utils.monitor;

import org.qiunet.utils.date.DateUtil;

import java.util.concurrent.TimeUnit;

/***
 *
 * @author qiunet
 * 2020-03-21 18:05
 **/
public class TestMonitor {
	private long curr = System.currentTimeMillis();

	private enum LogType {
		SHOP_BUY(1), MISSION(5), TASK(10);
		private int count;
		LogType(int count) {
			this.count = count;
		}

		public int getCount() {
			return count;
		}
	}

	private IMonitor<Long, LogType> monitor = new DefaultMonitor<>(
											LogType::getCount,
												(type, subType, num, delayTimes) -> {
													System.out.println((DateUtil.currentTimeMillis() - curr)+ "num ["+num+"] delayTimes ["+delayTimes+"]");
													return false;
												},
											1,
											TimeUnit.SECONDS

		);


	public void test(){
		long uid = 10000; int sleepMillis = 0;
		for (int i = 0; i < 12; i++) {
			monitor.add(uid, LogType.SHOP_BUY);

			int sleep = 990;
			if (i == 7) sleep = 11000;
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
