package org.qiunet.function.monitor;

/***
 * 返回某个小类的触发监控时间
 *
 * @author qiunet
 * 2020-05-13 12:42
 ***/
public interface ITriggerTimeMapping<SubType> {
	/**
	 * 返回某个小类的触发监控时间 (毫秒)
	 * @param subType
	 * @return
	 */
	long triggerTime(SubType subType);
}
