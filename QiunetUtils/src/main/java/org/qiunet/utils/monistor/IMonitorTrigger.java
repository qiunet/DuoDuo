package org.qiunet.utils.monistor;

/***
 * 触发调用
 *
 * @author qiunet
 * 2020-03-20 17:13
 ***/
public interface IMonitorTrigger<KEY, VAL> {
	/**
	 * 触发
	 * 如果已经处理. 需要调用 {@link IMonitorData#handler()}
	 * 否则调用 {@link IMonitorData#ignore()}
	 * @return
	 */
	void trigger(IMonitorData<KEY, VAL> data);
}
