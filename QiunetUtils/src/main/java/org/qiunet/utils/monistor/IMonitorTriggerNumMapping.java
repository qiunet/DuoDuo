package org.qiunet.utils.monistor;

/***
 *
 * 可以获取触发数.
 * 构造函数中使用.
 *
 * @author qiunet
 * 2020-03-20 17:13
 ***/
public interface IMonitorTriggerNumMapping<VAL> {
	/**
	 * 获得 val 对应的触发次数
	 * @param val
	 * @return
	 */
	long triggerNum(VAL val);
}
