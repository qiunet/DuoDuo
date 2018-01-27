package org.qiunet.utils.timer;

/**
 * Created by qiunet.
 * 18/1/27
 */
public class TimerTest {
	public static void main(String[] args) throws InterruptedException {
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

		Thread.sleep(8000);
	}
}
