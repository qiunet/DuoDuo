package org.qiunet.utils.thread;

/***
 * 线程安全的校验接口.
 *
 * @author qiunet
 * 2020-12-28 16:58
 */
public interface IThreadSafe {
	/**
	 * 判断线程是否安全.
	 * 如果执行不是在当前对象的线程. 可能有线程安全风险.
	 * 所以像消耗. 发奖. 需要判断下.
	 * @return true or false
	 */
	boolean inSelfThread();
}
