package org.qiunet.function.monitor;

import org.qiunet.utils.date.DateUtil;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/***
 * 监控数据
 * @author qiunet
 * 2020-03-20 18:01
 ***/
 class MonitorData<KEY, VAL> implements IMonitorData<KEY, VAL> {
	AtomicInteger delayTimes = new AtomicInteger();
	AtomicLong num = new AtomicLong();
	private long ignoreStartCheckTime;
	private long startCheckTime;
	private final KEY key;
	private final VAL val;


	public MonitorData(KEY key, VAL val) {
		this.resetDelayStartCheckTime();
		this.resetStartCheckTime();
		this.key = key;
		this.val = val;
	}



	@Override
	public KEY getType() {
		return key;
	}

	@Override
	public VAL getSubType() {
		return val;
	}

	@Override
	public int delayTimes() {
		return delayTimes.get();
	}

	@Override
	public long triggerNum() {
		return num.get();
	}

	void resetDelayStartCheckTime(){
		this.ignoreStartCheckTime = DateUtil.currentTimeMillis();
	}

	void resetStartCheckTime(){
		this.startCheckTime = DateUtil.currentTimeMillis();
		this.num.set(0);
	}

	long getStartCheckTime() {
		return startCheckTime;
	}

	long getIgnoreStartCheckTime() {
		return ignoreStartCheckTime;
	}

	void reset() {
		this.delayTimes.set(0);
	}

	void delayIt() {
		this.delayTimes.incrementAndGet();
	}

	void resetMonitorTime(){
		this.resetDelayStartCheckTime();
		this.resetStartCheckTime();
	}

	@Override
	public String toString() {
		return "MonitorData{" +
			"key=" + key +
			", val=" + val +
			", num=" + num.get() +
			", delayTimes=" + delayTimes.get() +
			'}';
	}
}
