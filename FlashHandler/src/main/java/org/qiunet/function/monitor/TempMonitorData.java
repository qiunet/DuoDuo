package org.qiunet.function.monitor;

/***
 *
 * @author qiunet
 * 2020-03-20 18:01
 ***/
class TempMonitorData<KEY, VAL> implements IMonitorData<KEY, VAL> {
	private final KEY key;
	private final VAL val;
	private final long num;
	private final int delayTimes;

	TempMonitorData(KEY key, VAL val, long num, int delayTimes) {
		this.key = key;
		this.val = val;
		this.num = num;
		this.delayTimes = delayTimes;
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
		return delayTimes;
	}

	@Override
	public long triggerNum() {
		return num;
	}

	@Override
	public String toString() {
		return "MonitorData{" +
			"key=" + key +
			", val=" + val +
			", num=" + num +
			", delayTimes=" + delayTimes +
			'}';
	}
}
