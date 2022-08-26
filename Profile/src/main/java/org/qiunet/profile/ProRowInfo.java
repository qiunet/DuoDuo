package org.qiunet.profile;

import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLongArray;

/***
 *  监控的行信息
 *
 * @author qiunet
 * 2020-11-04 11:36
 */
class ProRowInfo<Key, Column extends Enum<Column> & IProColumn> {
	private final Key key;
	private final Column [] columns;
	private final AtomicLongArray values;
	private final AtomicIntegerArray counts;
	private final AtomicLongArray totalValues;

	ProRowInfo(Key key, Column[] columns) {
		this.key = key;
		this.columns = columns;
		this.values = new AtomicLongArray(columns.length);
		this.counts = new AtomicIntegerArray(columns.length);
		this.totalValues = new AtomicLongArray(columns.length);
	}

	String getKey() {
		return key.toString();
	}

	String getValue(int i){
		return String.valueOf(values.get(i));
	}

	/**
	 * 记录数据.
	 * @param row 数据行
	 */
	void record(Profile.Row row) {
		for (int i = 0; i < row.getValues().length; i++) {
			long newValue = row.getValues()[i];
			ProStrategy strategy = columns[i].getStrategy();
			if (strategy == ProStrategy.avg) {
				int count = counts.incrementAndGet(i);
				long total = totalValues.accumulateAndGet(i, newValue, Long::sum);
				values.getAndUpdate(i, oldVal -> strategy.reAdd(oldVal, newValue, total, count));
			}else {
				values.getAndUpdate(i, oldVal -> strategy.reAdd(oldVal, newValue, 0, 0));
			}
		}
	}
}
