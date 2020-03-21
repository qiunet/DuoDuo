package org.qiunet.utils.monitor;

/***
 * 每个具体的监控对象
 *
 * @author qiunet
 * 2020-03-20 17:34
 ***/
public interface IMonitorData<Type, SubType> {

	/**
	 * 监控类型  一般是用户id
	 * @return
	 */
	Type getType();

	/**
	 * 监控子项
	 *
	 * @return
	 */
	SubType getSubType();

	/***
	 * 触发次数
	 * @return
	 */
	long triggerNum();

	/**
	 * 忽略的次数
	 * @return
	 */
	int ignoreNum();
}
