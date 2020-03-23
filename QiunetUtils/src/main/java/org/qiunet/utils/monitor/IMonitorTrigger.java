package org.qiunet.utils.monitor;

/***
 * 触发调用
 *
 * @author qiunet
 * 2020-03-20 17:13
 ***/
public interface IMonitorTrigger<Type, SubType> {
	/**
	 * 触发
	 * 里面的逻辑需要自行处理并发. 放到自己的 MessageHandler 处理.
	 *
	 * 如果已经处理.  返回 true
	 * 否则延后处理	 返回false
	 *
	 * @return true 处理了该key, key的忽略次数会清空
	 *  	  false 忽略报警. 忽略次数加 1
	 * 	 			有时候, 可能是第一次记录日志.
	 * 				第N次. 会踢出 然后封号等等.
	 * 	  			后 5 个触发时间内. 没有触发. 就会清空次数
	 */
	boolean trigger(Type type, SubType subType, long num, int delayTimes);
}
