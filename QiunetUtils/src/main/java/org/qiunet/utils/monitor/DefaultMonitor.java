package org.qiunet.utils.monitor;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.qiunet.utils.async.future.DFuture;
import org.qiunet.utils.date.DateUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.timer.TimerManager;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/***
 *
 * 监控器的默认实现.
 *
 * @author qiunet
 * 2020-03-20 18:07
 ***/
public class DefaultMonitor<Type, SubType> implements IMonitor<Type, SubType> {
	/***
	 * 忽略次数清空, 需要的等待的时间次数系数
	 */
	static final int IGNORE_COUNT_CLEAN_FAC = 5;


	private final Map<SubType, Long> triggerNumMap = Maps.newConcurrentMap();
	/**
	 * 统计结果的Map
	 */
	private final Map<Type, Map<SubType, MonitorData<Type, SubType>>> statistics = Maps.newConcurrentMap();

	private IMonitorTriggerNumMapping<SubType> numMapping;

	private IMonitorTrigger<Type, SubType> trigger;

	private int triggerTime;
	private TimeUnit unit;

	private long triggerMillis;
	/***
	 * 以分为单位构造该类
	 *
	 * @param numMapping val对应的触发次数限制. 外部提供
	 * @param trigger 触发器. 外部提供
	 * @param triggerTime 触发时间间隔
	 */
	public DefaultMonitor(IMonitorTriggerNumMapping<SubType> numMapping,
						  IMonitorTrigger<Type, SubType> trigger,
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
	public DefaultMonitor(IMonitorTriggerNumMapping<SubType> numMapping,
						  IMonitorTrigger<Type, SubType> trigger,
						  int triggerTime,
						  TimeUnit unit) {
		this.triggerMillis = unit.toMillis(triggerTime);
		this.numMapping = numMapping;
		this.triggerTime = triggerTime;
		this.trigger = trigger;
		this.unit = unit;
	}

	@Override
	public IMonitorData<Type, SubType> add(Type type, SubType subType, long num) {
		Preconditions.checkArgument(num > 0 , "num [%s] is less than 1", num);
		long triggerNum = triggerNumMap.computeIfAbsent(subType, k -> numMapping.triggerNum(k));
		if (triggerNum <= 0) return null;

		Map<SubType, MonitorData<Type, SubType>> subData = this.statistics.computeIfAbsent(type, k -> Maps.newConcurrentMap());
		MonitorData<Type, SubType> monitorData = subData.computeIfAbsent(subType, key -> new MonitorData<>(type, subType));
		long newNum = monitorData.num.addAndGet(num);

		if (newNum >= triggerNum) {
			long now = DateUtil.currentTimeMillis();

			if (now - monitorData.getIgnoreStartCheckTime() > IGNORE_COUNT_CLEAN_FAC * triggerMillis) {
				monitorData.resetDelayStartCheckTime();
				monitorData.delayTimes.set(0);
			}

			if (now - monitorData.getStartCheckTime() < triggerMillis) {
				DFuture<Boolean> future = TimerManager.getInstance().executorNow(() ->
					trigger.trigger(monitorData.getType(), monitorData.getSubType(), newNum, monitorData.delayTimes())
				);

				future.whenComplete((ret, e) -> {
					if (e != null) {
						LoggerType.DUODUO.error("DefaultMonitor Exception: ", e);
					}else {
						if (ret) monitorData.reset();
						else monitorData.delayIt();
					}
				});
				monitorData.resetMonitorTime();
			} else {
				monitorData.resetStartCheckTime();
			}
		}
		return monitorData;
	}


	@Override
	public int getTriggerTime() {
		return triggerTime;
	}

	@Override
	public TimeUnit getUnit() {
		return unit;
	}
}
