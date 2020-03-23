package org.qiunet.utils.monitor;

import java.util.concurrent.TimeUnit;

/***
 * 监视器. 长连接时候用比较合适.
 *
 * 可以监控用户包请求频率.
 * 获得物品的频率
 * 某类型的消耗  获得频率等等
 *
 * 通过单位时间{@link IMonitor#getTriggerTime()}内,
 * KEY的VAL类型的数量达到{@link IMonitorTriggerNumMapping#triggerNum(Object)}进行触发.
 *
 * 触发讲调用 {@link IMonitorTrigger#trigger(Object, Object, long, int)} )}
 * @author qiunet
 * 2020-03-20 16:51
 ***/
public interface IMonitor<Type, SubType> {
	/**
	 * 进行行为操作加 1
	 * @param type
	 * @param subType
	 */
	default void add(Type type, SubType subType){
		this.add(type, subType, 1);
	}
	/**
	 * 增加指定数量的行为统计
	 * @param type
	 * @param subType
	 * @param num
	 */
	void add(Type type, SubType subType, long num);
	/***
	 * 触发的时间间隔.
	 * 建议分为单位.
	 * @return
	 */
	int getTriggerTime();
	/***
	 * 时间单位
	 * @return
	 */
	TimeUnit getUnit();
}
