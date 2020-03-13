package org.qiunet.utils.timer.cd;

import org.qiunet.utils.date.DateUtil;

/***
 *
 *
 * @author qiunet
 * 2020-03-02 10:59
 ***/
class Timer {
	/**
	 * 下一次时间
	 */
	private long nextTime;
	/**
	 * 间隔
	 */
	private long period;


	Timer(long period) {
		this.period = period;
		this.nextTime = DateUtil.currentTimeMillis() +period;
	}

	Timer(ICdType cdType) {
		this(cdType.unit().toMillis(cdType.period()));
	}

	/**
	 * 如果cd失效, 重新cd
	 * @param renew 如果已经失效, 重新计时.
	 * @return 失效则返回true, 表示可以进行下面的逻辑. 并且已经重新cd
	 */
	 boolean isTimeout(boolean renew) {
		long millieTime = DateUtil.currentTimeMillis();
		if (validTimeout(millieTime)) {
			if (renew) {
				nextTime = millieTime + period;
			}
			return true;
		}
		return false;
	}

	/**
	 * 校验. 是否cd已经失效
	 * @param millieTime
	 * @return
	 */
	private boolean validTimeout(long millieTime) {
		return millieTime >= nextTime;
	}

	/**
	 * 得到cd剩余秒数
	 * @return
	 */
	 int getLeftSeconds() {
		long now = DateUtil.currentTimeMillis();
		if (validTimeout(now)) {
			return 0;
		}
		return (int) ((nextTime - now) / 1000);
	}

	long getNextTime() {
		return nextTime;
	}
}
