package org.qiunet.profile.reference;

import java.util.concurrent.atomic.AtomicLong;

/***
 * 单行数据详情
 *
 * @author qiunet
 * 2022/8/25 20:55
 */
class ReferenceRow<Key> {
	/**
	 * max, min count, total
	 */
	private final AtomicLong max = new AtomicLong();
	private final AtomicLong min = new AtomicLong();
	private final AtomicLong count = new AtomicLong();
	private final AtomicLong total = new AtomicLong();

	private final Key key;
	ReferenceRow(Key key) {
		this.key = key;
	}

	public Key getKey() {
		return key;
	}

	public long getMax() {
		return max.get();
	}

	public long getMin() {
		return min.get();
	}

	public long getCount() {
		return count.get();
	}

	public long getTotal() {
		return total.get();
	}

	public long getAvg() {
		if (getCount() == 0) {
			return 0;
		}
		return getTotal() / getCount();
	}
	/**
	 * 记录数据
	 * @param data
	 */
	public void record(long data) {
		if (getMin() == 0) {
			min.set(data);
		}
		max.accumulateAndGet(data, Long::max);
		min.accumulateAndGet(data, Long::min);
		count.incrementAndGet();
		total.addAndGet(data);
	}

}
