package org.qiunet.utils.monitor;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/***
 *
 *
 * @author qiunet
 * 2020-03-20 18:01
 ***/
public class MonitorData<KEY, VAL> implements IMonitorData<KEY, VAL> {
	AtomicInteger ignoreNum = new AtomicInteger();
	AtomicLong num = new AtomicLong();
	private long ignoreStartCheckTime;
	private long startCheckTime;
	private KEY key;
	private VAL val;


	public MonitorData(KEY key, VAL val) {
		this.resetIgnoreStartCheckTime();
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
	public int ignoreNum() {
		return ignoreNum.get();
	}

	@Override
	public long triggerNum() {
		return num.get();
	}

	void resetIgnoreStartCheckTime(){
		this.ignoreStartCheckTime = System.currentTimeMillis();
	}

	void resetStartCheckTime(){
		this.startCheckTime = System.currentTimeMillis();
		this.num.set(0);
	}

	long getStartCheckTime() {
		return startCheckTime;
	}

	long getIgnoreStartCheckTime() {
		return ignoreStartCheckTime;
	}

	void handler() {
		this.resetIgnoreStartCheckTime();
		this.resetStartCheckTime();
		this.ignoreNum.set(0);
	}

	void ignore() {
		this.ignoreNum.incrementAndGet();
		this.resetIgnoreStartCheckTime();
		this.resetStartCheckTime();
	}

	@Override
	public String toString() {
		return "MonitorData{" +
			"key=" + key +
			", val=" + val +
			", num=" + num.get() +
			", ignoreNum=" + ignoreNum.get() +
			'}';
	}
}
