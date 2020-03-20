package org.qiunet.utils.monistor;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/***
 *
 *
 * @author qiunet
 * 2020-03-20 18:07
 ***/
public class DefaultMonitor<Key, Val> implements IMonitor<Key, Val> {
	/**
	 * 统计结果的Map
	 */
	private final Map<Key, Map<Val, MonitorData<Key, Val>>> statistics = Maps.newConcurrentMap();

	private IMonitorTriggerNumMapping<Val> numMapping;

	private IMonitorTrigger<Key, Val> trigger;

	private int triggerTime;

	private TimeUnit unit;

	/***
	 * 以分为单位构造该类
	 *
	 * @param numMapping val对应的触发次数限制. 外部提供
	 * @param trigger 触发器. 外部提供
	 * @param triggerTime 触发时间间隔
	 */
	public DefaultMonitor(IMonitorTriggerNumMapping<Val> numMapping,
						  IMonitorTrigger<Key, Val> trigger,
						  int triggerTime) {
		this(numMapping, trigger, triggerTime, TimeUnit.MINUTES);
	}

	/**
	 *
	 * @param numMapping val对应的触发次数限制. 外部提供
	 * @param trigger 触发器. 外部提供
	 * @param triggerTime 触发时间间隔
	 * @param unit 	时间单位
	 */
	public DefaultMonitor(IMonitorTriggerNumMapping<Val> numMapping,
						  IMonitorTrigger<Key, Val> trigger,
						  int triggerTime,
						  TimeUnit unit) {
		this.numMapping = numMapping;
		this.trigger = trigger;
		this.triggerTime = triggerTime;
		this.unit = unit;
	}

	@Override
	public void add(Key key, Val val, long num) {

	}

	@Override
	public int triggerTime() {
		return triggerTime;
	}

	@Override
	public TimeUnit unit() {
		return unit;
	}
}
