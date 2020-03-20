package org.qiunet.utils.monistor;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/***
 *
 *
 * @author qiunet
 * 2020-03-20 18:01
 ***/
public final class MonitorData<KEY, VAL> implements IMonitorData<KEY, VAL> {
	private KEY key;
	private VAL val;

	private AtomicLong num;

	private AtomicInteger ignoreNum;

	@Override
	public KEY getKey() {
		return key;
	}

	@Override
	public VAL getVal() {
		return val;
	}

	@Override
	public long triggerNum() {
		return num.get();
	}

	@Override
	public void handler() {
		this.ignoreNum.set(0);
	}

	@Override
	public void ignore() {
		this.ignoreNum.incrementAndGet();
	}
}
