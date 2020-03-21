package org.qiunet.utils.monitor;

/***
 *
 * 可以获取触发数.
 * 构造函数中使用.
 *
 * @author qiunet
 * 2020-03-20 17:13
 ***/
public interface IMonitorTriggerNumMapping<SubType> {
	/**
	 * 获得 subType 对应的触发次数
	 * @param subType
	 * @return
	 */
	long triggerNum(SubType subType);
}
