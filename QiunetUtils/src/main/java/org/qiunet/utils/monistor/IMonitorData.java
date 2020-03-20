package org.qiunet.utils.monistor;

/***
 * 每个具体的监控对象
 *
 * @author qiunet
 * 2020-03-20 17:34
 ***/
public interface IMonitorData<Key, Val> {

	/**
	 * 监控key  一般是用户id
	 * @return
	 */
	Key getKey();

	/**
	 * 监控子项
	 *
	 * @return
	 */
	Val getVal();

	/***
	 * 触发次数
	 * @return
	 */
	long triggerNum();

	/**
	 * 处理了该key, key的忽略次数会清空
	 */
	void handler();

	/**
	 * 忽略报警. 忽略次数加 1
	 *
	 * 有时候, 可能是第一次记录日志.
	 * 第N次. 会踢出 然后封号等等.
	 *
	 * 后 5 个触发时间内. 没有触发. 就会清空次数
	 */
	void ignore();
}
