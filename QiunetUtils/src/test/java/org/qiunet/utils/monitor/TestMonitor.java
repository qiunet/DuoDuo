package org.qiunet.utils.monitor;

import java.util.concurrent.TimeUnit;

/***
 *
 * @author qiunet
 * 2020-03-21 18:05
 **/
public class TestMonitor {

	private enum LogType {
		SHOP_BUY(2), MISSION(5), TASK(10);
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
											data -> {
													System.out.println(data);
													return false;
												},
											200,
											TimeUnit.MILLISECONDS

		);


	public void test(){
		long uid = 10000;
		for (int i = 0; i < 12; i++) {
			monitor.add(uid, LogType.SHOP_BUY);

			int sleep = 80;
			if (i >= 10) sleep = 1100;
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 最后一次. 如果去 DefaultMonitor 打印. 会发现已经重置 ignoreNum
		}
	}

	public static void main(String[] args) {
		TestMonitor monitor = new TestMonitor();
		monitor.test();
	}
}
