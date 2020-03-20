package org.qiunet.utils.monistor;

import java.util.concurrent.TimeUnit;

/***
 * 监视器. 长连接时候用比较合适.
 *
 * 可以监控用户包请求频率.
 * 获得物品的频率
 * 某类型的消耗  获得频率等等
 *
 * 通过单位时间{@link IMonitor#triggerTime()}内,
 * KEY的VAL类型的数量达到{@link IMonitorTriggerNumMapping#triggerNum(Object)}进行触发.
 *
 * 触发讲调用 {@link IMonitorTrigger#trigger(IMonitorData)}
 * @author qiunet
 * 2020-03-20 16:51
 ***/
public interface IMonitor<KEY, VAL> {
	/**
	 * 进行行为操作加 1
	 * @param key
	 * @param val
	 */
	default void add(KEY key, VAL val){
		this.add(key, val, 1);
	}
	/**
	 * 增加指定数量的行为统计
	 * @param key
	 * @param val
	 * @param num
	 */
	void add(KEY key, VAL val, long num);
	/***
	 * 触发的时间间隔.
	 * 建议分为单位.
	 * @return
	 */
	int triggerTime();
	/***
	 * 时间单位
	 * @return
	 */
	TimeUnit unit();
}
