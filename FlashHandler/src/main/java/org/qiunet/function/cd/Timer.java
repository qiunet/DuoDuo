package org.qiunet.function.cd;

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
	/**
	 * 次数
	 */
	private int cfgCount;
	/**
	 * 记录次数
	 */
	private int recordCount;

	Timer(long period, int cfgCount) {
		this.period = period;
		this.cfgCount = cfgCount;
	}
	/**
	 * 记录cd
	 */
	void recordCd(){
		long timeMillis = DateUtil.currentTimeMillis();
		if (validTimeout(timeMillis) || this.nextTime == 0) {
			this.nextTime =  timeMillis + period;
			this.recordCount = 0;
		}
		this.recordCount ++;
	}
	/**
	 * 如果cd失效, 重新cd
	 * @return 失效则返回true, 表示可以进行下面的逻辑. 并且已经重新cd
	 */
	 boolean isTimeout() {
		 long millieTime = DateUtil.currentTimeMillis();
		if (this.validTimeout(millieTime)) {
			return true;
		}
		// 在cd内. 看次数
		return recordCount < cfgCount;
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

	/**
	 * 次数
	 * @return
	 */
	public int getRecordCount() {
		return recordCount;
	}
}
